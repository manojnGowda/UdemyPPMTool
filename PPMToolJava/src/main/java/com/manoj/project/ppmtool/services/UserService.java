package com.manoj.project.ppmtool.services;

import com.manoj.project.ppmtool.Exception.UsernameAlreadyExistException;
import com.manoj.project.ppmtool.domain.User;
import com.manoj.project.ppmtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User user){

        User result = null;
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setConfirmPassword("");
            result = userRepository.save(user);
        } catch (Exception e) {
            throw new UsernameAlreadyExistException("The user name'"+user.getUsername()+"' already exists");
        }
        return result;
    }
}
