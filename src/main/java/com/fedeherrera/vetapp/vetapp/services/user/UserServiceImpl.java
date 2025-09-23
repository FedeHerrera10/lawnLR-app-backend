package com.fedeherrera.vetapp.vetapp.services.user;

import com.fedeherrera.vetapp.vetapp.dtos.enums.ConfirmationResult;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOResetP;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOResetPassword;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOUserCreate;
import com.fedeherrera.vetapp.vetapp.dtos.request.DTOUserUpdate;
import com.fedeherrera.vetapp.vetapp.dtos.response.DTOUserResponse;
import com.fedeherrera.vetapp.vetapp.entities.ConfirmationToken;
import com.fedeherrera.vetapp.vetapp.entities.ImageProfile;
import com.fedeherrera.vetapp.vetapp.entities.Role;
import com.fedeherrera.vetapp.vetapp.entities.User;
import com.fedeherrera.vetapp.vetapp.exceptions.EntityExistException;
import com.fedeherrera.vetapp.vetapp.repositories.ConfirmationTokenRepository;
import com.fedeherrera.vetapp.vetapp.repositories.ImageProfileRepository;
import com.fedeherrera.vetapp.vetapp.repositories.RoleRepository;
import com.fedeherrera.vetapp.vetapp.repositories.UserRepository;
import com.fedeherrera.vetapp.vetapp.services.googleAuth.GoogleTokenVerifier;
import com.fedeherrera.vetapp.vetapp.services.mail.EmailService;
import com.fedeherrera.vetapp.vetapp.utils.MapperUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public DTOUserResponse save(DTOUserCreate user) {
        User userToSave = mapper.mapEntityToDto(user, User.class);
        userToSave.setEmail(userToSave.getEmail().toLowerCase());

        if (repository.existsByUsername(userToSave.getUsername())) {
            throw new EntityExistException("El usuario ya existe.");
        }
        if (repository.existsByEmail(userToSave.getEmail())) {
            throw new EntityExistException("El email ya está registrado.");
        }

        List<Role> roles = assignRoles(userToSave, user.getRoles());
        userToSave.setRoles(new HashSet<>(roles));

        String rawPassword = user.getPassword();

        if (!isRoleClient(userToSave)) {
            // Admin o Veterinario
            if (rawPassword == null || rawPassword.isEmpty()) {
                throw new DataIntegrityViolationException("La contraseña no puede estar vacia!");
            }
            userToSave.setPassword_expired(true);
            ;
            userToSave.setEnabled(true);
        } else {
            // Cliente
            userToSave.setPassword_expired(false);
            userToSave.setEnabled(false); // espera confirmación
        }

        userToSave.setPassword(passwordEncoder.encode(rawPassword));
        repository.save(userToSave);

        // Generar token de confirmación solo si es cliente

        ConfirmationToken token = new ConfirmationToken(userToSave);
        confirmationTokenRepository.save(token);
        sendConfirmationEmail(userToSave, token);

        return mapper.mapEntityToDto(userToSave, DTOUserResponse.class);
    }

    @Override
    @Transactional
    public User saveGoogleUser(String idToken) {
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(idToken);
        if (payload == null)
            return null;

        String email = payload.getEmail();
        String firstname = (String) payload.get("given_name");
        String lastname = (String) payload.get("family_name");

        // Revisar si ya existe usuario
        User userFromGoogle = repository.findByEmailIgnoreCase(email);

        if (userFromGoogle == null) {
            User u = new User();
            u.setEmail(email);
            u.setEnabled(true);
            u.setPassword_expired(false);
            u.setFirstname(firstname);
            u.setLastname(lastname);
            List<Role> roles = assignRoles(u, Map.of("cliente", true));
            u.setRoles(new HashSet<>(roles));
            return repository.save(u);
        }
        return userFromGoogle;
    }

    @Override
    @Transactional(readOnly = true)
    public DTOUserResponse getUserLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = authentication.getName();

        User user = repository.findByUsernameOrEmailIgnoreCase(username, username);
        if (user == null) {
            return null;
        }

        return mapper.mapEntityToDto(user, DTOUserResponse.class);
    }

    @Transactional
    @Override
    public boolean resetPassword(DTOResetP dtoResetP) {
        User user = repository.findByEmailIgnoreCase(dtoResetP.getEmail());
        if (user == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        Long idUser = user.getId();
        confirmationTokenRepository.deleteAllByUser_Id(idUser);
        //user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(dtoResetP.getPassword()));
        repository.save(user);

        //ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        //confirmationTokenRepository.save(oConfirmationToken);

       // sendResetPasswordEmail(user, oConfirmationToken);

        return true;
    }

    @Override
    @Transactional
    public boolean newCode(String email) {
        User user = repository.findByEmailIgnoreCase(email);

        if (user == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }

        if (user.isEnabled()) {
            throw new IllegalStateException("La cuenta ya está confirmada.");
        }

        Long idUser = user.getId();

        // Chequear último token
        Optional<ConfirmationToken> lastTokenOpt = confirmationTokenRepository
                .findFirstByUser_IdOrderByCreatedDateDesc(idUser);
        if (lastTokenOpt.isPresent()) {
            LocalDateTime lastCreated = lastTokenOpt.get().getCreatedDate();
            if (lastCreated.plusMinutes(1).isAfter(LocalDateTime.now())) {
                throw new IllegalStateException("Espere un minuto antes de solicitar otro código.");
            }
        }

        // Eliminar anteriores y generar nuevo
        confirmationTokenRepository.deleteAllByUser_Id(idUser);
        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);

        sendNewCodeEmail(user, oConfirmationToken);

        return true;
    }

    @Override
    @Transactional
    public boolean newCodeForChangePassword(String email) {
        User user = repository.findByEmailIgnoreCase(email);

        if (user == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }

        Long idUser = user.getId();

        // Chequear último token
        Optional<ConfirmationToken> lastTokenOpt = confirmationTokenRepository
                .findFirstByUser_IdOrderByCreatedDateDesc(idUser);
        if (lastTokenOpt.isPresent()) {
            LocalDateTime lastCreated = lastTokenOpt.get().getCreatedDate();
            if (lastCreated.plusMinutes(1).isAfter(LocalDateTime.now())) {
                throw new IllegalStateException("Espere un minuto antes de solicitar otro código.");
            }
        }

        // Eliminar anteriores y generar nuevo
        confirmationTokenRepository.deleteAllByUser_Id(idUser);
        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);

        sendNewCodeEmail(user, oConfirmationToken);

        return true;
    }

    @Override
    public List<DTOUserResponse> findAll() {
        return mapper.mapList(repository.findAll(), DTOUserResponse.class);
    }

    @Override
    public DTOUserResponse findById(Long id) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        ImageProfile imageProfile = imageProfileRepository.findByUserId(id).orElse(null);
        DTOUserResponse dtoUserResponse = mapper.mapEntityToDto(user, DTOUserResponse.class);
         if (imageProfile != null) {
         dtoUserResponse.setImageProfile(imageProfile.getImageData());
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
    public ConfirmationResult confirmAccount(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken).orElse(null);

        if (token == null) {
            return ConfirmationResult.NOT_FOUND;
        }
        // boolean timeTokenValidate =
        // TimeValidation.validate(token.getCreatedDate().toString().replace(" ",
        // "T").trim());

        // if (!timeTokenValidate) {
        // return ConfirmationResult.EXPIRED;
        // }

        User user = repository.findByEmailIgnoreCase(token.getUser().getEmail());
        user.setEnabled(true);
        user.setPassword_expired(false);
        repository.save(user);
        confirmationTokenRepository.delete(token);
        return ConfirmationResult.SUCCESS;
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
            roles.add(roleRepository.findByName("ROLE_VETERINARY").orElseThrow());
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

    private void sendConfirmationEmail(User user, ConfirmationToken token) {
        String codigo = token.getConfirmationToken();
        String to = user.getEmail();
        String subject = "Lawn Tennis APP Confirma tu cuenta ";

        String htmlContent = String.format(
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "  <meta charset='UTF-8'>" +
                        "  <style>" +
                        "    body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }" +
                        "    .container { background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }"
                        +
                        "    .title { font-size: 20px; font-weight: bold; color: #333333; }" +
                        "    .code { font-size: 18px; font-weight: bold; color: #2d89ef; margin: 15px 0; }" +
                        "    .btn { display: inline-block; padding: 10px 20px; font-size: 16px; background-color: #2d89ef; color: #ffffff; text-decoration: none; border-radius: 5px; }"
                        +
                        "    .footer { font-size: 12px; color: #888888; margin-top: 20px; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='container'>" +
                        "    <p class='title'>Hola %s,</p>" +
                        "    <p>Gracias por registrarte en <b>Lawn Tennis APP</b>. Para activar tu cuenta, utiliza el siguiente código de verificación:</p>"
                        +
                        "    <p class='code'>%s</p>" +
                        "    <p class='footer'>Si no solicitaste esta cuenta, puedes ignorar este mensaje.</p>" +
                        "  </div>" +
                        "</body>" +
                        "</html>",
                user.getFirstname(),
                codigo);
        emailService.sendHtmlMessage(to, subject, htmlContent);
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
        String text = String.format("Hola %s, confirma tu cuenta ingresando el siguiente codigo: %s",
                user.getFirstname(),
                codigo);

        emailService.sendSimpleMessage(to, subject, text);
    }

}
