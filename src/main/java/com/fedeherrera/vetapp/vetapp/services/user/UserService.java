package com.fedeherrera.vetapp.vetapp.services.user;

import java.util.List;

import com.fedeherrera.vetapp.vetapp.dtos.request.DTOResetPassword;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOUserUpdate;
import com.fedeherrera.vetapp.vetapp.dtos.response.DTOUserResponse;
import com.fedeherrera.vetapp.vetapp.entities.User;

public interface UserService {
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    DTOUserResponse save(User user);
    User saveGoogleUser(String email);
    DTOUserResponse getUserLogged();
    boolean confirmAccount(String code);
    boolean resetPassword(DTOResetPassword resetPassword);
    boolean newCode(String email);
    List<DTOUserResponse> findAll();
    DTOUserResponse findById(Long id);
    DTOUserResponse updateUser(Long id,DTOUserUpdate dtoUserUpdate);
}
    