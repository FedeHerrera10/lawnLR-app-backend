package com.fedeherrera.lawn.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor    // ⇐ genera el constructor (String dni, Boolean esSocio)
@Builder
public class JugadorDTO {
    private String dni;
    private Boolean esSocio;  // en requests puede venir null; en responses lo completás
}
