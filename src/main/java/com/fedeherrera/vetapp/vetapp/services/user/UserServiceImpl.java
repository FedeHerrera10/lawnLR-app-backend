package com.fedeherrera.vetapp.vetapp.services.user;

import com.fedeherrera.vetapp.vetapp.dtos.request.DTOResetPassword;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOUserUpdate;
import com.fedeherrera.vetapp.vetapp.dtos.response.DTOUserResponse;
import com.fedeherrera.vetapp.vetapp.dtos.response.DTOVeterinaryInfo;
import com.fedeherrera.vetapp.vetapp.entities.ConfirmationToken;
import com.fedeherrera.vetapp.vetapp.entities.ImageProfile;
import com.fedeherrera.vetapp.vetapp.entities.Role;
import com.fedeherrera.vetapp.vetapp.entities.User;
import com.fedeherrera.vetapp.vetapp.entities.VeterinaryInformation;
import com.fedeherrera.vetapp.vetapp.repositories.ConfirmationTokenRepository;
import com.fedeherrera.vetapp.vetapp.repositories.ImageProfileRepository;
import com.fedeherrera.vetapp.vetapp.repositories.RoleRepository;
import com.fedeherrera.vetapp.vetapp.repositories.UserRepository;
import com.fedeherrera.vetapp.vetapp.repositories.VeterinaryInformationRepository;
import com.fedeherrera.vetapp.vetapp.services.googleAuth.GoogleTokenVerifier;
import com.fedeherrera.vetapp.vetapp.services.mail.EmailService;
import com.fedeherrera.vetapp.vetapp.utils.MapperUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.fedeherrera.vetapp.vetapp.services.user.MessageUserService.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private MapperUtil mapper;

    @Autowired
    private ImageProfileRepository imageProfileRepository;

    @Autowired
    private VeterinaryInformationRepository veterinaryInformationRepository;

    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    @Transactional
    public DTOUserResponse save(User user) {
        String passwordRaw = "";

        List<Role> roles = assignRoles(user, null);
        user.setRoles(new HashSet<>(roles));
        
        if (isRoleClient(user) == false) {
            user.setPassword_expired(false);
            passwordRaw = user.getPassword();
            user.setEnabled(true);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        repository.save(user);

         ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
         confirmationTokenRepository.save(oConfirmationToken);
         sendConfirmationEmail(user, oConfirmationToken, passwordRaw);
         return mapper.mapEntityToDto(user, DTOUserResponse.class); 
        
    }

    @Override
    @Transactional
    public User saveGoogleUser(String idToken) {
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(idToken);
        if (payload == null) return null;

        String email = payload.getEmail();
        String firstname = (String) payload.get("given_name");
        String lastname = (String) payload.get("family_name");


        // Revisar si ya existe usuario
        User userFromGoogle = repository.findByEmailIgnoreCase(email);
        
        if(userFromGoogle == null){
            User u = new User();
            u.setEmail(email);
            u.setEnabled(true);
            u.setPassword_expired(false);
            u.setFirstname(firstname);
            u.setLastname(lastname);
            List<Role> roles = assignRoles(u,Map.of("cliente", true));
            u.setRoles(new HashSet<>(roles));
            return repository.save(u);
        }
        return userFromGoogle;
    }

    @Override
    @Transactional(readOnly = true)
    public DTOUserResponse getUserLogged() {
        // Obtener la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> optionalUser = null;
        if(authentication ==null){
            return null;
        }
        // Extraer el username del usuario autenticado
        String username = authentication.getName();
       optionalUser=Optional.ofNullable(repository.findByUsernameOrEmailIgnoreCase(username, username));
        
       return mapper.mapEntityToDto(optionalUser.get(), DTOUserResponse.class);
    }

    @Transactional
    @Override
    public boolean resetPassword(DTOResetPassword dtoResetPassword) {
        User user = repository.findByUsername(dtoResetPassword.getUsername()).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND));
        Long idUser = user.getId();
        confirmationTokenRepository.deleteAllByUser_Id(idUser);
        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(dtoResetPassword.getPassword()));
        repository.save(user);

        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);

        sendResetPasswordEmail(user, oConfirmationToken);

        return true;
    }

    @Override
    public boolean newCode(String email) {
        User user = repository.findByEmailIgnoreCase(email);

        if (user == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }

        Long idUser = user.getId();
        confirmationTokenRepository.deleteAllByUser_Id(idUser);
        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);

        sendNewCodeEmail(user, oConfirmationToken);

        return true;
    }

    @Override
     public List<DTOUserResponse> findAll() {
      return  mapper.mapList(repository.findAll(), DTOUserResponse.class);
     }

     @Override
     public DTOUserResponse findById(Long id) {
        Optional<User> optionalUser = repository.findById(id);
        if(optionalUser.isEmpty()){
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        ImageProfile imageProfile = imageProfileRepository.findByUserId(id).orElse(null);
        VeterinaryInformation veterinaryInformation = veterinaryInformationRepository.findByUser(user).orElse(null);
        DTOUserResponse dtoUserResponse = mapper.mapEntityToDto(user, DTOUserResponse.class);
        if (imageProfile != null) {
            dtoUserResponse.setImageProfile(imageProfile.getImageData());
        }
        if(veterinaryInformation != null){
            dtoUserResponse.setVeterinaryInformation(mapper.mapEntityToDto(veterinaryInformation, DTOVeterinaryInfo.class));
        }
        return dtoUserResponse;
     }

     
     @Override
     @Transactional
     public DTOUserResponse updateUser(Long id, DTOUserUpdate dtoUserUpdate) {
        User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        user.setFirstname(dtoUserUpdate.getFirstname());
        user.setLastname(dtoUserUpdate.getLastname());
        user.setEmail(dtoUserUpdate.getEmail());
        user.setUsername(dtoUserUpdate.getUsername());

        // List<Role> roles = assignRoles(user);
        // user.setRoles(new HashSet<>(roles));
        user.setEnabled(dtoUserUpdate.isEnabled());

        return mapper.mapEntityToDto(repository.save(user), DTOUserResponse.class);
    }

   

    @Transactional
    @Override
    public boolean confirmAccount(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken).orElseThrow(
                () -> new EntityNotFoundException(CODE_NOT_FOUND));

        // boolean timeTokenValidate = TimeValidation.validate(token.getCreatedDate().toString().replace(" ", "T").trim());

        // if (!timeTokenValidate) {
        //     return ResponseEntity.badRequest().body(CODE_EXPIRED);
        // }

        User user = repository.findByEmailIgnoreCase(token.getUser().getEmail());
        user.setEnabled(true);
        user.setPassword_expired(false);
        repository.save(user);
        confirmationTokenRepository.delete(token);
            return true;
        }


     

     
        private List<Role> assignRoles(User user, Map<String, Boolean> flags) {
            List<Role> roles = new ArrayList<>();
        
            if (Boolean.TRUE.equals(flags.get("admin"))) {
                roles.add(roleRepository.findByName("ROLE_ADMIN").orElseThrow());
            }
            if (Boolean.TRUE.equals(flags.get("cliente"))) {
                roles.add(roleRepository.findByName("ROLE_CLIENT").orElseThrow());
            }
            if (Boolean.TRUE.equals(flags.get("veterinario"))) {
                roles.add(roleRepository.findByName("ROLE_VET").orElseThrow());
            }
        
            if (roles.isEmpty()) {
                // Por defecto asignamos ROLE_CLIENT
                roles.add(roleRepository.findByName("ROLE_CLIENT").orElseThrow());
            }
        
            return roles;
        }
        

    private boolean isRoleClient(User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(ROLE_CLIENT));
    }

    private void sendConfirmationEmail(User user, ConfirmationToken token, String passwordRaw) {
        String codigo = token.getConfirmationToken();
        String to = user.getEmail();
        String subject = CONFIRMATION_EMAIL_SUBJECT;

        String text = String.format("Hola %s, confirma tu cuenta ingresando el siguiente codigo: %s \n <a href=http://localhost:3000/app/auth/confirm-account>Has click aqui</a>", user.getFirstname(),codigo);

        if (isRoleClient(user) == false) {
            text = "";
            text = String.format(
                    "Hola %s, tu contraseña es %s recuerda cambiarla.",
                    user.getFirstname(), passwordRaw);
        }

        emailService.sendSimpleMessage(to, subject, text);
    }

    private void sendResetPasswordEmail(User user, ConfirmationToken token) {
        String codigo = token.getConfirmationToken();
        String to = user.getEmail();
        String subject = RESET_PASSWORD_EMAIL_SUBJECT;
        String text = String.format("Hola %s, confirma tu cuenta nuevamente ingresando el siguiente codigo: %s",
                user.getFirstname(), codigo);

        emailService.sendSimpleMessage(to, subject, text);
    }

    private void sendNewCodeEmail(User user, ConfirmationToken token) {
        String codigo = token.getConfirmationToken();
        String to = user.getEmail();
        String subject = NEW_CODE_EMAIL_SUBJECT;
        String text = String.format("Hola %s, confirma tu cuenta ingresando el siguiente codigo: %s", user.getFirstname(),
                codigo);

        emailService.sendSimpleMessage(to, subject, text);
    }

}
