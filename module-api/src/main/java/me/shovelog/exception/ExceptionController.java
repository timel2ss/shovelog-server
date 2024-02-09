package me.shovelog.exception;

import me.shovelog.exception.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ShovelogException.class)
    public ResponseEntity<ErrorResponse> handleShovelogException(ShovelogException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(ErrorResponse.builder()
                        .code(String.valueOf(e.getStatusCode()))
                        .message(e.getMessage())
                        .build());
    }
}
