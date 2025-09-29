package com.fedeherrera.lawn.app.dtos.request;

import java.sql.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOUserUpdate {

    @NotBlank(message = "El nombre de usuario no puede estar vacio!")
    private String username;

    @NotBlank(message = "El nombre no puede estar vacio!")
    private String firstname;
    
    @NotBlank(message = "El apellido no puede estar vacio!")
    private String lastname;

    @Email(message = "El email no es valido!") 
    private String email;
    
    @NotBlank(message = "El documento no puede estar vacio!")
    private String document;

    @NotNull(message = "La fecha de nacimiento no puede estar vacia!")
    private Date birthdate;

    private String numberPhone;
}
