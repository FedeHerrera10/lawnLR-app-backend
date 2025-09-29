package com.fedeherrera.lawn.app.services.role;

import java.util.Optional;

import com.fedeherrera.lawn.app.entities.Role;

public interface RoleService {
    public Role saveRole(Role role); 
    public Optional<Role> findByName(String name);
}
