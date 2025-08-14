package com.fedeherrera.vetapp.vetapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.fedeherrera.vetapp.vetapp.services.auth.JwtService;
import com.fedeherrera.vetapp.vetapp.services.user.UserService;


import com.fedeherrera.vetapp.vetapp.dtos.request.DTOGoogleLoginRequest;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOResetPassword;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

@Autowired
    private UserService service;

@Autowired
    private JwtService jwtService;
    
@PostMapping("/google")
public ResponseEntity<?> googleLogin(@RequestBody DTOGoogleLoginRequest request) {
    
    
    
    
    if (service.saveGoogleUser(request.idToken()) != null){
        String email = service.saveGoogleUser(request.idToken()).getEmail();
        String token = jwtService.generateToken(email);
        return ResponseEntity.ok(token);
    }
    
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login fallido.");
}

    @PutMapping(value="/confirm-account/{token}")
    public ResponseEntity<?> confirmUserAccount(@PathVariable String token) {
       if(service.confirmAccount(token)){
        return ResponseEntity.status(HttpStatus.OK).body("Cuenta Confirmada.");
       }
       return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de Google inválido");
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody DTOResetPassword dtoResetPassword) {
        boolean update = service.resetPassword(dtoResetPassword);
        if(!update) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo actualizar la contraseña.");
        return ResponseEntity.status(200).body("Contraseña actualizada");
    }

    @PostMapping("/new-code")
    public ResponseEntity<?> generateNewCode(@RequestBody Map<String, String> requestBody) {
        service.newCode(requestBody.get("email"));
        return ResponseEntity.status(200).body("Codigo enviado , revise su mail.");
    }
    

}
