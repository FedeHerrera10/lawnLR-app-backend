package com.fedeherrera.vetapp.vetapp.services.role;

import java.util.Optional;

import com.fedeherrera.vetapp.vetapp.entities.Role;

public interface RoleService {
    public Role saveRole(Role role); 
    public Optional<Role> findByName(String name);
}
