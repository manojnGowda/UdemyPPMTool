package com.manoj.project.ppmtool.controller;

import com.manoj.project.ppmtool.Security.JwtTokenProvider;
import com.manoj.project.ppmtool.Security.SecurityConstant;
import com.manoj.project.ppmtool.Validator.UserValidator;
import com.manoj.project.ppmtool.domain.Project;
import com.manoj.project.ppmtool.domain.User;
import com.manoj.project.ppmtool.payload.JWTLoginSucessResponse;
import com.manoj.project.ppmtool.payload.LoginRequest;
import com.manoj.project.ppmtool.services.ExceptionValidatorService;
import com.manoj.project.ppmtool.services.ProjectService;
import com.manoj.project.ppmtool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ExceptionValidatorService exceptionValidatorService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?>autheticate(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult){
        ResponseEntity response = exceptionValidatorService.validBindingResult(bindingResult);
        if(response!=null) return response;
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstant.TOKEN_PREFIX+jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTLoginSucessResponse(true,jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody User user, BindingResult bindingResult){
        userValidator.validate(user,bindingResult);
        ResponseEntity response = exceptionValidatorService.validBindingResult(bindingResult);
        if(response!=null) return response;

        User result = userService.saveUser(user);
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }
}
