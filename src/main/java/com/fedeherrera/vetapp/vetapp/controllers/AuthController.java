package com.fedeherrera.vetapp.vetapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.fedeherrera.vetapp.vetapp.services.auth.JwtService;
import com.fedeherrera.vetapp.vetapp.services.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.fedeherrera.vetapp.vetapp.dtos.enums.ConfirmationResult;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOEmail;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOGoogleLoginRequest;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOResetP;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOResetPassword;
import com.fedeherrera.vetapp.vetapp.exceptions.EntityExistException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Iniciar sesión con Google", description = "Autentica un usuario utilizando un token de Google.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas o error en la autenticación")
    })
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody DTOGoogleLoginRequest request) {

        if (service.saveGoogleUser(request.idToken()) != null) {
            String email = service.saveGoogleUser(request.idToken()).getEmail();
            String token = jwtService.generateToken(email);
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login fallido.");
    }

    @Operation(summary = "Confirmar cuenta", description = "Confirma la cuenta de un usuario utilizando un token de confirmación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta confirmada con éxito"),
            @ApiResponse(responseCode = "401", description = "Token inválido o expirado"),
            @ApiResponse(responseCode = "404", description = "Token no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error inesperado")
    })
    @PutMapping(value = "/confirm-account/{token}")
    public ResponseEntity<?> confirmUserAccount(@PathVariable String token) {
       ConfirmationResult result = service.confirmAccount(token);
        switch (result) {
        case SUCCESS:
            return ResponseEntity.ok("Cuenta confirmada con éxito.");
        
        case EXPIRED:
            throw new EntityExistException("El token ha expirado, solicita uno nuevo.");
        
        case NOT_FOUND:
            throw new EntityExistException("El token no es valido.");
        default:
            throw new EntityExistException("Error inesperado.");
    }
    }

    @Operation(summary = "Restablecer contraseña", description = "Restablece la contraseña de un usuario utilizando un token de confirmación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña restablecida con éxito"),
            @ApiResponse(responseCode = "400", description = "No se pudo actualizar la contraseña"),
            @ApiResponse(responseCode = "500", description = "Error inesperado")
    })
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody DTOResetP dtoResetPassword) {
        boolean update = service.resetPassword(dtoResetPassword);
        if (!update)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo actualizar la contraseña.");
        return ResponseEntity.status(200).body("Contraseña actualizada");
    }

    @Operation(summary = "Generar nuevo codigo", description = "Genera un nuevo codigo de confirmacion para un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Codigo enviado, revise su mail."),
    })
    @PostMapping("/new-code")
    public ResponseEntity<?> generateNewCode(@RequestBody DTOEmail dtoEmail) {
        service.newCode(dtoEmail.getEmail());
        return ResponseEntity.status(200).body("Codigo enviado , revise su mail.");
    }

    @Operation(summary = "Generar nuevo codigo", description = "Genera un nuevo codigo de confirmacion para un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Codigo enviado, revise su mail."),
    })
    @PostMapping("/new-code-for-change-password")
    public ResponseEntity<?> generateNewCodeForChangePassword(@RequestBody DTOEmail dtoEmail) {
        service.newCodeForChangePassword(dtoEmail.getEmail());
        return ResponseEntity.status(200).body("Codigo enviado , revise su mail.");
    }

}
