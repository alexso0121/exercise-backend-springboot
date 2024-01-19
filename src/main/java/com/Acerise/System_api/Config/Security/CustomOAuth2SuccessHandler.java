package com.Acerise.System_api.Config.Security;

import com.Acerise.System_api.Repository.UserRepository;
import com.Acerise.System_api.Service.AuthService;
import com.Acerise.System_api.Service.OAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private UserRepository repository;

    @Autowired
    private  OAuth2UserService oAuth2UserService;




    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        // Retrieve the user's email from the authentication object
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String email =token.getAuthorizedClientRegistrationId();
        String attr=request.getHeader("OAuthRequestUrl");
        log.info("requestID2"+request.getRequestedSessionId());

        //important
        String name = token.getPrincipal().toString();
        String picture = token.getCredentials().toString();
        log.info("name: " + name);
        log.info("email: " + email);
        log.info("picture: " + picture);
        log.info(response.getHeader("OAuth2RequestType"));
        log.info(response.getHeader("OAuth2Provider"));

       String redirectUrl= oAuth2UserService.oauth2Redirect(request,authentication,response);

        redirectStrategy.sendRedirect(request, response, redirectUrl);

//        super.onAuthenticationSuccess(request, response, authentication);
    }
}
