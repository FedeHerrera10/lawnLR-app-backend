package com.fedeherrera.vetapp.vetapp.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Excepcion para tipo de datos incorrectos en request
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Error en el formato de los datos enviados.");

        Throwable cause = ex.getCause();
        if (cause instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) cause;

            // Obtener detalles de los campos problemáticos
            StringBuilder camposConError = new StringBuilder();
            jsonMappingException.getPath().forEach(ref -> {
                camposConError.append(ref.getFieldName()).append(" ");
            });

            errorResponse.put("detalles", "Error en los campos: " + camposConError.toString().trim());
        } else {
            errorResponse.put("detalles", "El formato de los datos enviados es incorrecto o no se puede convertir.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocurrio un error. Verifique los datos enviados.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> methodNoSuported(HttpRequestMethodNotSupportedException ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> noResourceFound(NoResourceFoundException ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> sQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> missingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(NotReadablePropertyException.class)
    public ResponseEntity<Map<String, Object>> notReadablePropertyException(
            NotReadablePropertyException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> noHandlerFoundException(
            NoHandlerFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<Map<String, Object>> jpaSystemException(
            JpaSystemException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> dataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // =================== Metodos para manejar excepciones personalizadas
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProjectNotFoundException(EntityNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(EntityExistException.class)
    public ResponseEntity<Map<String, Object>> entityExistException(EntityExistException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Escucho de manera global las excepciones de validacion @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        BindingResult result = ex.getBindingResult();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(),
                error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocurrio un error. Verique los datos.");
    }

}