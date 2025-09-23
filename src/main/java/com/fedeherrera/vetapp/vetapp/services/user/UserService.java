package com.fedeherrera.vetapp.vetapp.services.user;

import java.util.List;

import com.fedeherrera.vetapp.vetapp.dtos.request.DTOResetP;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOUserCreate;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOUserUpdate;
import com.fedeherrera.vetapp.vetapp.dtos.response.DTOUserResponse;
import com.fedeherrera.vetapp.vetapp.entities.User;
import com.fedeherrera.vetapp.vetapp.dtos.enums.ConfirmationResult;

public interface UserService {
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    DTOUserResponse save(DTOUserCreate user);
    User saveGoogleUser(String email);
    DTOUserResponse getUserLogged();
    ConfirmationResult   confirmAccount(String code);
    boolean resetPassword(DTOResetP  resetPassword);
    boolean newCode(String email);
    boolean newCodeForChangePassword(String email);
    List<DTOUserResponse> findAll();
    DTOUserResponse findById(Long id);
    DTOUserResponse updateUser(Long id,DTOUserUpdate dtoUserUpdate);
}
    