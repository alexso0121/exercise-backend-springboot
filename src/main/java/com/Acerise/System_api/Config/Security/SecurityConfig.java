package com.Acerise.System_api.Config.Security;

import com.Acerise.System_api.Enum.RoleEnum;
import com.Acerise.System_api.Service.AuthService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    private final UserDetailService myUserDetailsService;



    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    boolean webSecurityDebug;
//    private final CustomAuthorizedClientService CustomAuthorizedClientService;

    public SecurityConfig(UserDetailService myUserDetailsService,  CustomOAuth2SuccessHandler customOAuth2SuccessHandler) {

        this.myUserDetailsService = myUserDetailsService;

        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
//        CustomAuthorizedClientService = customAuthorizedClientService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Return chain that allows any request and has no authentication
        return http.csrf(AbstractHttpConfigurer::disable)
               .cors(withDefaults())
                .addFilterBefore(new CustomAuthFilter(), OAuth2AuthorizationRequestRedirectFilter.class)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/**","/api/exercise/preview").permitAll();
                    auth.requestMatchers("/login/oauth2/**").permitAll();
                    auth.requestMatchers("/api/exercise/preview").permitAll();
             auth.requestMatchers("/api/exercise/uploadFiles","/api/exercise/uploadExercise").hasAnyAuthority("SCOPE_ADMIN");
                    auth.anyRequest().authenticated();
                })

                .oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults())

                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .userDetailsService(myUserDetailsService).httpBasic(withDefaults())


                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userAuthoritiesMapper(this.userAuthoritiesMapper())

                )
//                                .authorizedClientService(CustomAuthorizedClientService)
                                .successHandler(customOAuth2SuccessHandler)
//                        .defaultSuccessUrl("/api/auth/oauth2Redirect")
                       )
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))).build();

//

    }

    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                if (authority instanceof OidcUserAuthority oidcUserAuthority) {

                    OidcIdToken idToken = oidcUserAuthority.getIdToken();
                    OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
                    String email = oidcUserAuthority.getAttributes().get("email").toString();
                    log.info("email: " + email);


                    // Map the claims found in idToken and/or userInfo
                    // to one or more GrantedAuthority's and add it to mappedAuthorities

                } else if (authority instanceof OAuth2UserAuthority oauth2UserAuthority) {

                    Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();


                    // Map the attributes found in userAttributes
                    // to one or more GrantedAuthority's and add it to mappedAuthorities

                }
            });
            log.info("mappedAuthorities: " + mappedAuthorities);


            return mappedAuthorities;
        };
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public RequestCache requestCache() {
        return new HttpSessionRequestCache();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        log.info("authenticationManager");
        try {
            return authConfig.getAuthenticationManager();
        } catch (Exception exception) {
            System.out.println(exception);
            return null;
        }
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(webSecurityDebug);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder().issuer("http://localhost:8080").build();
    }


}