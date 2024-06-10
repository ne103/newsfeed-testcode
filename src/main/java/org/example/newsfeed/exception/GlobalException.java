package org.example.newsfeed.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalException {

    // Handle All Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        log.error(ex.getMessage());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("message", "서버 오류가 발생했습니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Valid 검증 오류
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("message", "필드에 대한 유효성 검사 실패");

        // 유효성 검사에 실패한 필드 및 메시지 수집
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
            log.error("필드에 대한 유효성 검사 실패: {} - {}", fieldName, errorMessage);
        });
        errorResponse.put("errors", fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleIOException(IOException ex) {
        log.error(ex.getMessage());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("message", "입출력 오류가 발생했습니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalAccessError.class)
    public ResponseEntity<Map<String, Object>> handleIllegalAccessError(IllegalAccessError ex) {
        log.error(ex.getMessage());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("message", "잘못된 접근입니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle DuplicateUserException
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateUserException(DuplicateUserException ex) {
        log.error(ex.getMessage());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", HttpStatus.CONFLICT.value());
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserIdNotFoundException(UserIdNotFoundException ex) {
        log.error(ex.getMessage());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", HttpStatus.NOT_FOUND.value());
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Map<String, Object>> handlePasswordMismatchException(PasswordMismatchException ex) {
        log.error(ex.getMessage());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyWithdrawnUserException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyWithdrawnUserException(AlreadyWithdrawnUserException ex) {
        log.error(ex.getMessage());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
