package com.fedeherrera.vetapp.vetapp.dtos.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data

public class DTOEmail {
    @Email
    private String email;
}
