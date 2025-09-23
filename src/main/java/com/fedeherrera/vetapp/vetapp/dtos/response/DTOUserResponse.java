package com.fedeherrera.vetapp.vetapp.dtos.response;

import java.time.LocalDateTime;
import java.util.Set;


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
    private boolean password_expired;
    private Set<DTORole> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // private LocalDateTime updated_at;
     private String imageProfile;
}
