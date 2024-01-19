package com.Acerise.System_api.Service;

import com.Acerise.System_api.Enum.Authprovider;
import com.Acerise.System_api.Exception.CommonException.CustomCommonBadRequestException;
import com.Acerise.System_api.Exception.CommonException.CustomCommonInternalServerException;
import com.Acerise.System_api.Model.User;
import com.Acerise.System_api.Repository.UserRepository;
import com.Acerise.System_api.dto.Auth.AuthResponse;
import com.Acerise.System_api.dto.Auth.LoginRequest;
import com.Acerise.System_api.dto.Auth.RegisterRequest;
import com.Acerise.System_api.dto.CustomResponse;

import com.Acerise.System_api.dto.User.UserUpdateRequest;
import com.Acerise.System_api.utils.EmailUtil;
import com.Acerise.System_api.utils.OtpUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;


import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService extends ApiHandler<User>    {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder encoder;
    private final ModelMapper modelMapper;
    private final OtpUtil otpUtil;
    private final EmailUtil VerificationEmailUtil;






    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtEncoder encoder, ModelMapper modelMapper, OtpUtil otpUtil, EmailUtil verificationEmailUtil) {
        this.userRepository = userRepository;
        this.authenticationManager=authenticationManager;
        this.passwordEncoder=passwordEncoder;
        this.encoder=encoder;
        this.modelMapper = modelMapper;

        this.otpUtil = otpUtil;
        this.VerificationEmailUtil = verificationEmailUtil;
    }
    @Override
    public MongoRepository<User, String> getRepository() {
        return userRepository;
    }





    @Transactional(transactionManager = "primaryTransactionManager",rollbackFor = Exception.class)
    public CustomResponse<String> regenerateOtp(String email) throws MessagingException {
        User existUser=userRepository.findUserByEmailAndProvider(email,"self").orElseThrow(()->new UsernameNotFoundException("User not found with email: "+email));
        String otp=otpUtil.generateOtp();
        existUser.setOtp(otp);
        existUser.setOtpGeneratedTime(LocalDateTime.now());
        ImplementSave(existUser);
        VerificationEmailUtil.sendOtpEmail(existUser.getEmail(), existUser.getId(), existUser.getOtp());
        return new CustomResponse<String>(
                "Otp regenerated successfully",
                HttpStatus.OK,
                null,
                LocalDateTime.now()
        );
    }


    @Transactional(transactionManager = "primaryTransactionManager",rollbackFor = Exception.class)
    public CustomResponse<AuthResponse> register(RegisterRequest registerDTO, HttpServletResponse response) {
        try{
            User existuser=userRepository.findById(registerDTO.getId()).orElseThrow(()->new UsernameNotFoundException("User not found with id: "+registerDTO.getId()));

            log.info("registerDTO: " + registerDTO.toString());

            String encoded_pwd=passwordEncoder.encode(registerDTO.getEncrypted_pwd());

            modelMapper.map(registerDTO,existuser);
            log.info(existuser.toString());
            existuser.setEncrypted_pwd(encoded_pwd);

            existuser.setProvider(Authprovider.self);

            User savedUser=ImplementSave(existuser);
            AuthResponse authResponse=modelMapper.map(savedUser,AuthResponse.class);



            Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    existuser.getEmail(),registerDTO.getEncrypted_pwd()));

            String token=generateToken(authentication);
            Cookie cookie=new Cookie("JWTTOKEN",token);
            cookie.setPath("/");
            cookie.setSecure(true);
            response.addCookie(cookie);





            return new CustomResponse<AuthResponse>(
                    authResponse,
                    HttpStatus.OK,
                    null,
                    LocalDateTime.now()

                    );

        }catch(Exception e){
            System.out.println(e.getMessage());
              throw new CustomCommonInternalServerException(e.getMessage());
        }

    }

    @Transactional
    public CustomResponse<String> activateOauthUser(UserUpdateRequest request){
        User user =findById(request.id());
        modelMapper.map(request,user);
        user.setEnable(true);
        ImplementSave(user);
        return new CustomResponse<String>(
                "User activated successfully",
                HttpStatus.OK,
                null,
                LocalDateTime.now()
        );


    }

    @Transactional
    public CustomResponse<String> verifyEmail(String id, String otp) {

            User existUser=userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found with id: "+id));

            if (existUser.getOtp().equals(otp)&& Duration.between(existUser.getOtpGeneratedTime(),
                    LocalDateTime.now()).getSeconds() < (5 * 60)){
                existUser.setEnable(true);
                ImplementSave(existUser);
                return CustomResponse.<String>builder()
                        .body("account with user id "+existUser.getId()+" activated successfully")
                        .message(null)
                        .status(HttpStatus.OK)
                        .dateTime(LocalDateTime.now())
                        .build();
            }else{
                throw new CustomCommonBadRequestException("Invalid otp");
            }

    }
    public CustomResponse<AuthResponse> login(LoginRequest loginRequest, HttpServletResponse response){
        try{
            Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.email(),loginRequest.encrypted_pwd()));

            String token=generateToken(authentication);

            User user=userRepository.findUserByEmailAndProvider(loginRequest.email(),"self").orElseThrow(()->new UsernameNotFoundException("User not found with email: "+loginRequest.email()));
            AuthResponse authResponse=modelMapper.map(user,AuthResponse.class);
            Cookie cookie=new Cookie("JWTTOKEN",token);
            cookie.setPath("/");
            cookie.setSecure(true);
            response.addCookie(cookie);

//            authResponse.setJwt(token);

            return CustomResponse.<AuthResponse>builder()
                    .body( authResponse)
                    .status(HttpStatus.OK)
                    .message(null)
                    .build();

        }catch(Exception e){
            throw new CustomCommonInternalServerException(e.getMessage());
        }

    }

    private String generateToken (Authentication authentication){
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        User authenticatedUser = (User) authentication.getPrincipal();
        String userId = authenticatedUser.getId();  // Assuming the ID is of type Long

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("Acerise-dev")
                .issuedAt(now)
                .expiresAt(now.plus(12, ChronoUnit.HOURS))
                .claim("email", authentication.getName())
                .claim("id", userId)
                .claim("scope",scope).build();
        //return the encoded jwt token
        return this.encoder.encode(JwtEncoderParameters.from((claims))).getTokenValue();
    }


    @Transactional(transactionManager = "primaryTransactionManager",rollbackFor = Exception.class)
    public CustomResponse<String> requestOtp(String email) {
        System.out.println(email);
        User existUser=userRepository.findUserByEmailAndProvider(email,"self").orElse(null);
        System.out.println(existUser);
        try{
        String otp=otpUtil.generateOtp();
        User user;
        User savedUser;

        if (existUser!=null && existUser.getProvider()==Authprovider.self){
            System.out.println(existUser.getPassword());
            if(existUser.getPassword()!=null){
                throw new CustomCommonInternalServerException("email has already been registered ");
            }

            existUser.setOtp(otp);
            existUser.setOtpGeneratedTime(LocalDateTime.now());
            savedUser=ImplementSave(existUser);

        }else{
            user=User.builder().email(email).otp(otp).
                    provider(Authprovider.self).
                    otpGeneratedTime(LocalDateTime.now()).isEnable(false).build();
            savedUser=ImplementSave(user);
        }




            VerificationEmailUtil.sendOtpEmail(savedUser.getEmail(), savedUser.getId(), savedUser.getOtp());


    } catch (MessagingException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        return new CustomResponse<String>(
                "Otp sent successfully",
                HttpStatus.OK,
                null,
                LocalDateTime.now()
        );
    }}

