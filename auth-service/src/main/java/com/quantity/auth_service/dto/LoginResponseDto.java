package com.quantity.auth_service.dto;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String jwt;
    Long id;
}
