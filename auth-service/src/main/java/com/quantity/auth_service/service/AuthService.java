package com.quantity.auth_service.service;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.quantity.auth_service.model.User;
import com.quantity.auth_service.repository.UserRepository;
import com.quantity.auth_service.dto.LoginResponseDto;
import com.quantity.auth_service.dto.SignupRequestDto;
import com.quantity.auth_service.dto.SignupResponseDto;
import com.quantity.auth_service.dto.LoginRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.quantity.auth_service.security.AuthUtil;
import com.quantity.auth_service.model.type.AuthProviderType;

import io.jsonwebtoken.Jwts;

import org.springframework.security.core.userdetails.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    public LoginResponseDto login(LoginRequestDto loginDto) {
        // triggers authentication flow
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword())

        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // if sucees it return userDetails
        User user = userRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = authUtil.generateAccessToken(user); // generate token
        System.out.println("Generated key " + token);
        return new LoginResponseDto(token, user.getId());

    }

    public SignupResponseDto signUp(SignupRequestDto signUpResponseDto) {
        User user = userRepo.findByUsername(signUpResponseDto.getUsername()).orElse(null);
        if (user != null) {
            throw new IllegalArgumentException("Username is already exist");
        }

        user = userRepo.save(User.builder().username(signUpResponseDto.getUsername())
                .password(encoder.encode(signUpResponseDto.getPassword())).role("ROLE_USER").build());
        return new SignupResponseDto(user.getId(), user.getUsername());
    }

    public User signUpInternal(SignupRequestDto signupRequestDto,
            AuthProviderType authProviderType,
            String providerId) {

        User existing = userRepo.findByUsername(signupRequestDto.getUsername()).orElse(null);

        if (existing != null) {
            return existing;
        }

        User user = User.builder()
                .username(signupRequestDto.getUsername())
                .providerId(providerId)
                .providerType(authProviderType)
                .role("ROLE_USER")
                .build();

        if (authProviderType == AuthProviderType.EMAIL) {
            user.setPassword(encoder.encode(signupRequestDto.getPassword()));
        } else {
            user.setPassword(encoder.encode(UUID.randomUUID().toString()));
        }

        return userRepo.save(user);
    }

    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        User user = signUpInternal(signupRequestDto, AuthProviderType.EMAIL, null);
        return new SignupResponseDto(user.getId(), user.getUsername());
    }

    @Transactional
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User,
            String registrationId) {

        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        String email = oAuth2User.getAttribute("email");

        User user = userRepo.findByProviderIdAndProviderType(providerId, providerType).orElse(null);

        if (user == null) {

            User emailUser = userRepo.findByUsername(email).orElse(null);

            if (emailUser != null) {
                user = emailUser;
            
                user.setProviderId(providerId);
                user.setProviderType(providerType);
            
              
                if (user.getPassword() == null) {
                    user.setPassword(encoder.encode(UUID.randomUUID().toString()));
                }
            
                userRepo.save(user);
            } else {
                String username = authUtil.determineUsernameFromOAuth2User(oAuth2User, registrationId, providerId);

                SignupRequestDto dto = new SignupRequestDto();
                dto.setUsername(username);

                user = signUpInternal(dto, providerType, providerId);
            }
        }

        String token = authUtil.generateAccessToken(user);

        return ResponseEntity.ok(new LoginResponseDto(token, user.getId()));
    }

}
