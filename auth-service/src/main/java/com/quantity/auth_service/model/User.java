package com.quantity.auth_service.model;

import jakarta.persistence.*;
import lombok.*;
import com.quantity.auth_service.model.type.AuthProviderType;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = true)
    private String password;

    private String role;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private AuthProviderType providerType;

}
