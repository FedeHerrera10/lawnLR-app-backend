package com.fedeherrera.lawn.app.services.user;

import java.util.List;

import com.fedeherrera.lawn.app.dtos.request.DTOResetP;
import com.fedeherrera.lawn.app.dtos.request.DTOUserCreate;
import com.fedeherrera.lawn.app.dtos.request.DTOUserUpdate;
import com.fedeherrera.lawn.app.dtos.response.DTOUserResponse;
import com.fedeherrera.lawn.app.entities.User;
import com.fedeherrera.lawn.app.dtos.enums.ConfirmationResult;

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
    