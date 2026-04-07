package com.quantity.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data

public class SignupRequestDto {
    @NotBlank
    private String username;
    private String password;

}
