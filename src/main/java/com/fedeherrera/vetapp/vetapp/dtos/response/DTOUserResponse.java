package com.fedeherrera.vetapp.vetapp.dtos.response;

import java.time.LocalDateTime;
import java.util.Set;

import com.fedeherrera.vetapp.vetapp.entities.Role; 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOUserResponse {

    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private boolean enabled;
    private boolean admin;
    private boolean cliente;
    private boolean veterinario;
    private boolean password_expired;
    private Set<Role> roles;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String imageProfile;
    private DTOVeterinaryInfo veterinaryInformation;
}
