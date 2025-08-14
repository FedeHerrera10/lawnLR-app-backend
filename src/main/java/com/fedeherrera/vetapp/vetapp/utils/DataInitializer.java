package com.fedeherrera.vetapp.vetapp.utils;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fedeherrera.vetapp.vetapp.entities.Role;
import com.fedeherrera.vetapp.vetapp.entities.Specialization;
import com.fedeherrera.vetapp.vetapp.entities.User;
import com.fedeherrera.vetapp.vetapp.repositories.SpecializationRepository;
import com.fedeherrera.vetapp.vetapp.repositories.UserRepository;
import com.fedeherrera.vetapp.vetapp.services.role.RoleService;
@Component
public class DataInitializer implements CommandLineRunner{

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        
        Role userRole = new Role();
        userRole.setName("ROLE_CLIENT");
        
        Role veterinaryRole = new Role();
        veterinaryRole.setName("ROLE_VETERINARY");

        roleService.saveRole(adminRole);
        roleService.saveRole(userRole);
        roleService.saveRole(veterinaryRole);

        initUsers();
        initSpecializations();
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
        clientUser.setEnabled(false);
        clientUser.setPassword_expired(false);
        HashSet<Role> roles2 = new HashSet<>();
        roles2.add(roleService.findByName("ROLE_CLIENT").get());
        clientUser.setRoles(new HashSet<>(roles2));
        userRepository.save(clientUser);
    }

    private void initSpecializations() {
        initSpecialization("Cardiology");
        initSpecialization("Dermatology");
        initSpecialization("Oncology");
        initSpecialization("Orthopedics");
        initSpecialization("Surgery");
        initSpecialization("Neurology");
        initSpecialization("Ophthalmology");
        initSpecialization("Radiology");
        initSpecialization("Internal Medicine");
        initSpecialization("Emergency");
    }

    private void initSpecialization(String name) {
        Specialization specialization = new Specialization();
        specialization.setName(name);
        specializationRepository.save(specialization);
    }
}   
