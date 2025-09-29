package com.fedeherrera.lawn.app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class User extends Audit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private boolean enabled;
    private boolean password_expired;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private String document;
    
    private Date birthdate;

    private String numberPhone;

    // Campos para seguridad y control de login
    private int failedLoginAttempts = 0;        // contador de intentos fallidos
    private LocalDateTime lastFailedLogin;      // fecha del último intento fallido

    // Método para consultar roles
    public boolean hasRole(String roleName) {
        return roles.stream()
                    .anyMatch(r -> r.getName().equalsIgnoreCase(roleName));
    }
}

