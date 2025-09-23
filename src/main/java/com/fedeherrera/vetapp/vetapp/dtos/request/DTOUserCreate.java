package com.fedeherrera.vetapp.vetapp.dtos.request;

import java.sql.Date;
import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Min(value = 10000000, message = "El documento debe tener al menos 8 digitos!")
    private Long document;

    @NotNull(message = "La fecha de nacimiento no puede estar vacia!")
    private Date birthdate;
   // Roles opcionales
   private Map<String, Boolean> roles;
}
