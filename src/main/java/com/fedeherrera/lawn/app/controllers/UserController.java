package com.fedeherrera.lawn.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fedeherrera.lawn.app.dtos.request.DTOImageUpload;
import com.fedeherrera.lawn.app.dtos.request.DTOUserCreate;
import com.fedeherrera.lawn.app.dtos.request.DTOUserUpdate;
import com.fedeherrera.lawn.app.dtos.response.DTOUserResponse;
import com.fedeherrera.lawn.app.services.imageProfile.ImageProfileService;
import com.fedeherrera.lawn.app.services.user.UserService;
import com.fedeherrera.lawn.app.exceptions.EntityExistException;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private ImageProfileService imageProfileService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DTOUserCreate user) {
        if (service.existsByUsername(user.getUsername()))
            throw new EntityExistException("El usuario ya existe en el sistema.");
        if (service.existsByEmail(user.getEmail()))
            throw new EntityExistException("El email ya existe en el sistema.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con éxito", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Usuario o email ya existentes en el sistema"),
            @ApiResponse(responseCode = "500", description = "Error inesperado")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody DTOUserCreate user) {
        return create(user);
    }

    @Operation(summary = "Obtener usuario actual", description = "Obtiene el usuario actualmente autenticado.")
    @GetMapping("/")
    public ResponseEntity<?> me() {
        DTOUserResponse user = service.getUserLogged();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> list(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody DTOUserUpdate user) {
        return ResponseEntity.ok(service.updateUser(id, user));
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestBody DTOImageUpload imageRequest) {
        imageProfileService.saveImage(imageRequest.getUserId(), imageRequest.getImageBase64());
        return ResponseEntity.ok("Imagen guardada exitosamente");
    }
}
