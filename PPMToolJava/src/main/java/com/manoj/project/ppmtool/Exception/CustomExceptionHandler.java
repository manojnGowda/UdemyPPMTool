package com.manoj.project.ppmtool.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object>exceptionHandler(ProjectIdException e, WebRequest request){
        ProjectIdExceptionResponse response = new ProjectIdExceptionResponse(e.getMessage());
        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object>projectNotFoundExceptionHandler(ProjectNotFoundException e, WebRequest request){
        ProjectNotFoundExceptionResponse response = new ProjectNotFoundExceptionResponse(e.getMessage());
        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object>usernameAlreadyExistExceptionHandler(UsernameAlreadyExistException e, WebRequest request){
        UsernameAlreadyExistExceptionResponse response = new UsernameAlreadyExistExceptionResponse(e.getMessage());
        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }



}
