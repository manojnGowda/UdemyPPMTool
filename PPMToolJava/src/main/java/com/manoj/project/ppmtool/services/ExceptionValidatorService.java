package com.manoj.project.ppmtool.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExceptionValidatorService {
    public ResponseEntity<?> validBindingResult(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<Map<String,String>>(bindingResult.getFieldErrors().stream().collect(Collectors.toMap(error -> error.getField(), error -> error.getDefaultMessage(),(a1,a2)->a1)),HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
