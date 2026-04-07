package com.quantity.auth_service.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.quantity.auth_service.service.AuthService;
import com.quantity.auth_service.dto.LoginResponseDto;
import com.quantity.auth_service.dto.SignupRequestDto;
import com.quantity.auth_service.dto.LoginRequestDto;
import com.quantity.auth_service.dto.SignupResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
// @CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestLoginDto) {
        LoginResponseDto responseDto = authService.login(requestLoginDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signUp(@Valid @RequestBody SignupRequestDto signUpRequestDto) {
        SignupResponseDto responseDto = authService.signUp(signUpRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

    }

}
