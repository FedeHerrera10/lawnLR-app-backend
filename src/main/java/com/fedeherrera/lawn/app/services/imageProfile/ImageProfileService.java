package com.fedeherrera.lawn.app.services.imageProfile;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fedeherrera.lawn.app.entities.ImageProfile;
import com.fedeherrera.lawn.app.entities.User;
import com.fedeherrera.lawn.app.repositories.ImageProfileRepository;
import com.fedeherrera.lawn.app.repositories.UserRepository; 

import jakarta.persistence.EntityNotFoundException;

@Service
public class ImageProfileService {
    @Autowired
    private ImageProfileRepository imageProfileRepository;

    @Autowired
    private UserRepository userRepository;

    public ImageProfile saveImage(Long userId, String base64Data) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        User user = userOpt.get();

        // Verificar si ya existe una imagen para el usuario
        Optional<ImageProfile> existingImage = imageProfileRepository.findByUserId(userId);
        ImageProfile imageProfile = existingImage.orElse(new ImageProfile());

        imageProfile.setImageData(base64Data);
        imageProfile.setUser(user);

        return imageProfileRepository.save(imageProfile);
    }
}
