package com.prime.rush_hour.exception_handling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({EmailExistsException.class})
    public ResponseEntity<Object> handleEmailExistsException(EmailExistsException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({AdminCannotBeModifiedException.class})
    public ResponseEntity<Object> handleAdminCannotBeModifiedException(AdminCannotBeModifiedException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({InvalidUserException.class})
    public ResponseEntity<Object> handleInvalidUserException(InvalidUserException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({RequestStreamException.class})
    public ResponseEntity<Object> handleRequestStreamException(RequestStreamException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Arguments are not valid", errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }
}
