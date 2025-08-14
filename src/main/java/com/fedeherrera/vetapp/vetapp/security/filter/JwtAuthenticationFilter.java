package com.fedeherrera.vetapp.vetapp.security.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fedeherrera.vetapp.vetapp.dtos.request.DTOResetPassword;
import com.fedeherrera.vetapp.vetapp.entities.User;
import com.fedeherrera.vetapp.vetapp.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.fedeherrera.vetapp.vetapp.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    static final  String ERROR_INVALID_USERNAME_PASSWORD = "Usuario o Contraseña incorrectos!";
    static final  String ERROR_ACCOUNT_DISABLED = "Su cuenta no esta habilitada!";
    static final  String ERROR_LOCKED = "Su cuenta esta bloqueada!";
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 15;

    private UserRepository userRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {

    DTOResetPassword loginRequest;
    try {
        loginRequest = new ObjectMapper().readValue(request.getInputStream(), DTOResetPassword.class);
        request.setAttribute("loginUserName", loginRequest.getUsername());
    } catch (IOException e) {
        throw new AuthenticationServiceException("Error al leer datos de login", e);
    }

    User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);

    // Verificar bloqueo temporal
    if (user != null && user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS &&
        user.getLastFailedLogin() != null &&
        user.getLastFailedLogin().isAfter(LocalDateTime.now().minusMinutes(LOCK_MINUTES))) {
        
        throw new AuthenticationServiceException("Cuenta bloqueada temporalmente por intentos fallidos");
    }
    
    UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

    return authenticationManager.authenticate(authToken);
}


@Override
protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
        FilterChain chain, Authentication authResult) throws IOException, ServletException {

    // Obtenemos el usuario autenticado
    org.springframework.security.core.userdetails.User user =
            (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

    String username = user.getUsername();

    // Serializamos roles como lista de strings
    List<String> rolesList = authResult.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    
      // Resetear contador de intentos fallidos
      userRepository.findByUsername(username).ifPresent(u -> {
        u.setFailedLoginAttempts(0);
        userRepository.save(u);
    });

    // Creamos Claims para el JWT
    Claims claims = Jwts.claims()
    .add("authorities", new ObjectMapper().writeValueAsString(rolesList))
    .add("username", username)
    .build();


    // Generamos el token
    // Remove the .setSubject(username) line since it's already in claims
    String token = Jwts.builder()
    .subject(username)
    .claims(claims)  // Use claims() instead of setClaims()
    .issuedAt(new Date())
    .expiration(new Date(System.currentTimeMillis() + 3600000))
    .signWith(SECRET_KEY)
    .compact();

    // Creamos el cuerpo de la respuesta
    Map<String, Object> body = new HashMap<>();
    body.put("token", token);
    body.put("username", username);
    body.put("roles", rolesList);
    body.put("message", "Hola " + username + ", has iniciado sesión con éxito!");

    // Respondemos al cliente
    response.setContentType(CONTENT_TYPE);
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
}


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
    AuthenticationException failed) throws IOException, ServletException {
        String errorMString = failed.getMessage();
        String username =   (String) request.getAttribute("loginUserName");
       

        if (failed instanceof BadCredentialsException) {
            errorMString = ERROR_INVALID_USERNAME_PASSWORD;
        } else if (failed instanceof DisabledException) {
            errorMString = ERROR_ACCOUNT_DISABLED;
        } else if (failed instanceof LockedException) {
            errorMString = ERROR_LOCKED;
        }

        // Incrementar intentos fallidos si el usuario existe
        if (username != null && !username.isEmpty()) {
            userRepository.findByUsername(username).ifPresent(u -> {
                u.setFailedLoginAttempts(u.getFailedLoginAttempts() + 1);
                u.setLastFailedLogin(LocalDateTime.now());
                userRepository.save(u);
            });
        }

        Map<String, String> body = new HashMap<>();
        body.put("message", errorMString);

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(401);
    }
    

}
