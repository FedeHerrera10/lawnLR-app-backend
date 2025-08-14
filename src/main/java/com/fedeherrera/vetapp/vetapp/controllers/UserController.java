package com.fedeherrera.vetapp.vetapp.controllers;

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

import com.fedeherrera.vetapp.vetapp.dtos.request.DTOImageUpload;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOUserCreate;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOUserUpdate;
import com.fedeherrera.vetapp.vetapp.entities.User;
import com.fedeherrera.vetapp.vetapp.services.imageProfile.ImageProfileService;
import com.fedeherrera.vetapp.vetapp.services.user.UserService;
import com.fedeherrera.vetapp.vetapp.utils.MapperUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    
    @Autowired
    private UserService service;
    
    @Autowired
    private MapperUtil mapper;

    @Autowired
    private ImageProfileService imageProfileService;
    
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DTOUserCreate user) {
        
        User userToSave = mapper.mapEntityToDto(user, User.class);
        if(service.existsByUsername(user.getUsername())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya existe en el sistema.");
        if(service.existsByEmail(user.getEmail())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El email ya existe en el sistema.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userToSave));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody DTOUserCreate user) {
        return create(user);
    }

    @GetMapping("/")
    public ResponseEntity<?> me() {
        return ResponseEntity.ok(service.getUserLogged());
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
