package com.fedeherrera.lawn.app.dtos.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data

public class DTOEmail {
    @Email
    private String email;
}
