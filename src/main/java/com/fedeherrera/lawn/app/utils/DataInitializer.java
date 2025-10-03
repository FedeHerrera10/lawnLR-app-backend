package com.fedeherrera.lawn.app.utils;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fedeherrera.lawn.app.entities.Role;
import com.fedeherrera.lawn.app.entities.User;
import com.fedeherrera.lawn.app.entities.Cancha;
import com.fedeherrera.lawn.app.repositories.UserRepository;
import com.fedeherrera.lawn.app.repositories.CanchaRepository;
import com.fedeherrera.lawn.app.services.role.RoleService;
@Component
public class DataInitializer implements CommandLineRunner{

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CanchaRepository canchaRepository;


    @Override
    public void run(String... args) throws Exception {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        
        Role userRole = new Role();
        userRole.setName("ROLE_USUARIO");
        

        roleService.saveRole(adminRole);
        roleService.saveRole(userRole);

        initUsers();
        initCanchas();
    }

    private void initUsers() {
     
         User adminUser = new User();
         adminUser.setFirstname("Federico");
         adminUser.setLastname("Herrera");
         adminUser.setEmail("herrera3299@gmail.com");
         adminUser.setPassword(passwordEncoder.encode("12345678"));
         adminUser.setUsername("fherrera10");
         adminUser.setEnabled(true);
         adminUser.setPassword_expired(false);
         HashSet<Role> roles = new HashSet<>();
         roles.add(roleService.findByName("ROLE_ADMIN").get());
         adminUser.setRoles(new HashSet<>(roles));    
         userRepository.save(adminUser);
        
        User clientUser = new User();
        clientUser.setFirstname("Celeste");
        clientUser.setLastname("Gomez");
        clientUser.setEmail("celeste222@gmail.com");
        clientUser.setPassword(passwordEncoder.encode("12345678"));
        clientUser.setUsername("celeste123");
        clientUser.setEnabled(true);
        clientUser.setPassword_expired(false);
        HashSet<Role> roles2 = new HashSet<>();
        roles2.add(roleService.findByName("ROLE_USUARIO").get());
        clientUser.setRoles(new HashSet<>(roles2));
        userRepository.save(clientUser);
    }

    private void initCanchas() {
        Cancha cancha = new Cancha();
        cancha.setNombre("Cancha 1");
        canchaRepository.save(cancha);

        Cancha cancha2 = new Cancha();
        cancha2.setNombre("Cancha 2");
        canchaRepository.save(cancha2);

        Cancha cancha3 = new Cancha();
        cancha3.setNombre("Cancha 3");
        canchaRepository.save(cancha3);
    }    
}   
