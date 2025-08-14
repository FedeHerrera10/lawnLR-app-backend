package com.fedeherrera.vetapp.vetapp.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOUserCreate {

    @NotBlank(message = "El nombre de usuario no puede estar vacio!")
    private String username;

    @NotBlank(message = "El nombre no puede estar vacio!")
    private String firstname;
    
    @NotBlank(message = "El apellido no puede estar vacio!")
    private String lastname;

    @Email(message = "El email no es valido!") 
    private String email;
    
    @NotBlank(message = "La contraseña no puede estar vacia!")
    private String password;
    
    private boolean enabled;

    private boolean admin;

    private boolean cliente;

    private boolean veterinario;

    private boolean password_expired;
}
