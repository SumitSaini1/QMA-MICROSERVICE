package com.quantity.auth_service.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.quantity.auth_service.dto.LoginResponseDto;
import com.quantity.auth_service.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    @Lazy

    private AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
                                        
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        System.out.println("Success handler hit");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("OAuth SUCCESS HANDLER HIT");

        String registrationId = token.getAuthorizedClientRegistrationId();

        LoginResponseDto loginResponse =
                authService.handleOAuth2LoginRequest(oAuth2User, registrationId).getBody();

        String jwt = loginResponse.getJwt();;

        // String redirectUrl = "http://localhost:5501/index.html?token=" + jwt;
        String redirectUrl = "https://qmamicroservice.netlify.app/login?token=" 
        + URLEncoder.encode(jwt, StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}