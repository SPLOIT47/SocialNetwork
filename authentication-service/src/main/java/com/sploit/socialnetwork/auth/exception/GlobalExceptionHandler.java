package com.sploit.socialnetwork.auth.exception;

import com.sploit.socialnetwork.auth.payload.response.ApiError;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            RoleNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ApiError> handleRoleNotFoundException(RuntimeException e) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, e.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ApiError> handleTokenRefreshException(TokenRefreshException e) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, e.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(UserAlreadyExists userAlreadyExists) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, userAlreadyExists.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e) {
        String stackTrace = ExceptionUtils.getStackTrace(e);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, stackTrace);
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
