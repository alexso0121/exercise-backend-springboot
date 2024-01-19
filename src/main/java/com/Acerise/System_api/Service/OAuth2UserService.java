package com.Acerise.System_api.Service;


import com.Acerise.System_api.Enum.Authprovider;
import com.Acerise.System_api.Exception.CommonException.CustomCommonInternalServerException;
import com.Acerise.System_api.Model.User;
import com.Acerise.System_api.Repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;


@Slf4j
@Service
public class OAuth2UserService extends ApiHandler<User>{

    private final UserRepository userRepository;

    private final JwtEncoder encoder;

    @Value("${client-domain}")
    private String clientDomain;



    //    private final RequestCache requestCache;
    public OAuth2UserService(UserRepository userRepository, JwtEncoder encoder) {
        this.userRepository = userRepository;

        this.encoder = encoder;
    }

    @Transactional
    public String oauth2Redirect(HttpServletRequest request, Authentication oAuth2AuthenticationToken, HttpServletResponse response){
        log.info(" start oauth2Redirect");
        Cookie[] cookies = request.getCookies();
        String oauth2Request="";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("OAuth2Request".equals(cookie.getName())) {
                    oauth2Request = cookie.getValue();
                    // Use the cookie value
                    System.out.println("Cookie Value: " + oauth2Request);
                    break;
                }


            }
        }
        String[] authRequest=oauth2Request.split("/");
        String provider=authRequest[0];
        String requestType=authRequest[1];
        String redirectUrl="";

        if(Objects.equals(requestType, "login")) {
             redirectUrl = OAuth2login(oAuth2AuthenticationToken, response, provider);
        }else {
            redirectUrl = OAuth2Register(oAuth2AuthenticationToken, response, provider);
            log.info("redirectUrl: " + redirectUrl);
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }



        return redirectUrl;


    }

    private String OAuth2login(Authentication authentication,HttpServletResponse response,String provider){
        try{
            String email="";
            if (authentication.getPrincipal() instanceof DefaultOidcUser defaultOidcUser){
                email = defaultOidcUser.getEmail();
            }else if(authentication.getPrincipal() instanceof DefaultOAuth2User defaultOAuth2User){
                email = defaultOAuth2User.getAttribute("email");
            }
            log.info("email: " + email);
            User existUser=userRepository.findUserByEmailAndProvider(email,provider).orElse(null);
            if(existUser!=null){
                log.info("authSuccess");

                String token=generateToken(authentication,existUser.getId());
                Cookie cookie=new Cookie("JWTTOKEN",token);
                cookie.setPath("/");
                cookie.setMaxAge(3600); // Set the maximum age of the cookie in seconds

                response.addCookie(cookie);
                return clientDomain;
            }else{
                log.info("oauth2 fail");
                SecurityContextHolder.clearContext();
                return clientDomain+"?loginerror=0";

            }





        }catch(Exception e){
            throw new CustomCommonInternalServerException(e.getMessage());
        }
    }

    private String OAuth2Register(Authentication authentication,HttpServletResponse response,String provider){
        try{
            String email="";
            if (authentication.getPrincipal() instanceof DefaultOidcUser defaultOidcUser){
                 email = defaultOidcUser.getEmail();
            }else if(authentication.getPrincipal() instanceof DefaultOAuth2User defaultOAuth2User){
                log.info("defaultOAuth2User: " + defaultOAuth2User.getAttributes());
                email = defaultOAuth2User.getAttribute("email");

            }

            log.info("email: " + email);
            User existUser=userRepository.findUserByEmailAndProvider(email,provider).orElse(null);
            if(existUser!=null){
                log.info("user already exist");

                String token=generateToken(authentication, existUser.getId());
                Cookie cookie=new Cookie("JWTTOKEN",token);
                cookie.setPath("/");
                cookie.setSecure(true);

                response.addCookie(cookie);
                return clientDomain;
            }else{
                log.info("oauth2 register");
                User user=User.builder().
                        email(email).
                        provider(Authprovider.valueOf(provider)).
                        isEnable(true).
                        build();
                User saveduser=ImplementSave(user);
                String token=generateToken(authentication,saveduser.getId());
                Cookie cookie=new Cookie("JWTTOKEN",token);
                cookie.setPath("/");
                cookie.setSecure(true);
                response.addCookie(cookie);

                return clientDomain;

            }
            }catch(Exception e){
            throw new CustomCommonInternalServerException(e.getMessage());
        }

    }

    private String generateToken (Authentication authentication,String userId){
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

          // Assuming the ID is of type Long

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





    @Override
    public MongoRepository<User, String> getRepository() {
        return userRepository;
    }

    //map picture to user from oauth2
}