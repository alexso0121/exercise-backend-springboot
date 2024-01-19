package com.Acerise.System_api.Config.Security;

import com.Acerise.System_api.Exception.CommonException.CustomCommonBadRequestException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Configuration
@Slf4j
public class CustomAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("start custom filter");

        String requestURI = request.getRequestURI();

        log.info("requestURI: " + requestURI);
        if(requestURI.startsWith("/oauth2/authorization/")){
            log.info("hi");
            String oAuth2Provider = requestURI.substring("/oauth2/authorization/".length());
            log.info("oauth2Provider: " + oAuth2Provider);
            String oauth2RequestType=request.getParameter("requestType");
            if(oauth2RequestType==null){
                throw new CustomCommonBadRequestException("oauth2RequestType not found");
            }
            Cookie cookie = new Cookie("OAuth2Request",oAuth2Provider+"/"+oauth2RequestType);
//            cookie.setHttpOnly(true);
            cookie.setMaxAge(3600); // Set the maximum age of the cookie in seconds
            cookie.setPath("/");
            // Set the path for which the cookie is valid

            response.addCookie(cookie);

            log.info("oauth2RequestType: " + oauth2RequestType);
//            Cookie cookie2 = new Cookie("OAuth2RequestType",oauth2RequestType);
//            cookie.setMaxAge(3600); // Set the maximum age of the cookie in seconds
//            cookie.setPath("/");
//            response.addCookie(cookie2);

        }


        filterChain.doFilter(request, response);
    }
}
