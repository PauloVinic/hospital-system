package com.techchallenge.agendamentoservice.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null 
                            ? fieldError.getDefaultMessage() 
                            : "Campo inválido",
                        (existing, replacement) -> existing,
                        LinkedHashMap::new // Mantém ordem dos campos
                ));

        return ResponseEntity.badRequest().body(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Erro de validação nos campos",
                errors
        ));
    }

    @ExceptionHandler({EntityNotFoundException.class, BusinessException.class})
    public ResponseEntity<ErrorResponse> handleBusinessErrors(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericErrors(Exception ex) {
        Map<String, String> details = new LinkedHashMap<>();
        details.put("erro", ex.getMessage()); // Adiciona mensagem original para debug em dev
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado",
                details
        ));
    }

    public record ErrorResponse(
            LocalDateTime timestamp,
            HttpStatus status, // Alterado para HttpStatus (mais semântico)
            String message,
            Map<String, String> details // LinkedHashMap mantém ordem de inserção
    ) {
        public ErrorResponse(LocalDateTime timestamp, HttpStatus status, String message) {
            this(timestamp, status, message, null);
        }
    }
}