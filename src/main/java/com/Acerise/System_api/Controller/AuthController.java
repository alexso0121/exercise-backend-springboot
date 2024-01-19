package com.Acerise.System_api.Controller;

import com.Acerise.System_api.Service.AuthService;
import com.Acerise.System_api.dto.Auth.AuthResponse;
import com.Acerise.System_api.dto.Auth.LoginRequest;
import com.Acerise.System_api.dto.Auth.RegisterRequest;
import com.Acerise.System_api.dto.Auth.RequestOtp;
import com.Acerise.System_api.dto.CustomResponse;
import com.Acerise.System_api.dto.User.UserUpdateRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public CustomResponse<AuthResponse> register(@RequestBody RegisterRequest registerDTO, HttpServletResponse response){

        System.out.println("registerDTO: " + registerDTO.toString());

        return authService.register(registerDTO,response);
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }
    @PostMapping("/login")
    public CustomResponse<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        log.info("loginRequest: " + loginRequest.toString());

        return authService.login(loginRequest,response);
    }



    @GetMapping("/verifyEmail")
    public CustomResponse<String> verifyEmail(@RequestParam String id, @RequestParam String otp){
        log.info("token: " + id);
        return authService.verifyEmail(id, otp);
    }

    @PostMapping("/requestOtp")
    public CustomResponse<String> requestOtp(@RequestBody RequestOtp requestOtp)  {
        log.info("token: " + requestOtp.getEmail());
        return authService.requestOtp(requestOtp.getEmail());
    }


    @PostMapping("oauth2/activate")
    public CustomResponse<String> oauth2Activate(@RequestBody UserUpdateRequest request){
        log.info("loginRequest: " + request.toString());
        return authService.activateOauthUser(request);
    }

    @GetMapping("/regenerateOtp")
    public CustomResponse<String> regenerateOtp(@RequestParam String email) throws MessagingException {
        log.info("token: " + email);
        return authService.regenerateOtp(email);
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    //add cache on session request




}
