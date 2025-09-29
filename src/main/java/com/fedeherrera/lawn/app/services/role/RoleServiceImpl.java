package com.fedeherrera.lawn.app.services.role;

import java.util.Optional;

import com.fedeherrera.lawn.app.entities.Role;
import com.fedeherrera.lawn.app.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public Role saveRole(Role role) {
        Optional<Role> roleOptional = findByName(role.getName());
        if(!roleOptional.isPresent()){
            return roleRepository.save(role);
        }
        return roleOptional.get();
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

 

    
}
