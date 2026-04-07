package com.quantity.quantity_service.exception;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<HashMap<String,Object> >handleRuntimeException(RuntimeException ex) {
        HashMap<String,Object> error=new HashMap<>();
        error.put("message",ex.getMessage());
        error.put("status",400);
        error.put("timestamp",System.currentTimeMillis());

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity
                .internalServerError()
                .body("Something went wrong: " + ex.getMessage());
    }
}