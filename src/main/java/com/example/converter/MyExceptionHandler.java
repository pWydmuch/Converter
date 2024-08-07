package com.example.converter;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConverterToArabic.BadRomanNumberException.class,
    ConverterToRoman.BadArabicNumberException.class})
    public ResponseEntity<?> handleUserBadNumbers(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleInvalidCharacters(Exception e) {
        return new ResponseEntity<>("You can use digits only", HttpStatus.BAD_REQUEST);
    }
}