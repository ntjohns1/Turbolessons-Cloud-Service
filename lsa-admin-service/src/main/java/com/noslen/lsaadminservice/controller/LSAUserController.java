package com.noslen.lsaadminservice.controller;

import com.noslen.lsaadminservice.model.LSAUser;
import com.noslen.lsaadminservice.repository.LSAUserRepository;
import com.noslen.lsaadminservice.util.auth.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RefreshScope
public class LSAUserController {
    @Autowired
    private LSAUserRepository userRepository;

    //GET ALL USERS
    @GetMapping("/api/users")
    @ResponseStatus(HttpStatus.OK)
    public List<LSAUser> getAllUsers() {
        return userRepository.findAll();
    }

    //GET USER BY ID
    @GetMapping("/api/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LSAUser getUserById(@PathVariable Integer id) {
        Optional<LSAUser> returnVal = userRepository.findById(id);
        return returnVal.orElse(null);
    }
    @PostMapping("/api/users")
    @ResponseStatus(HttpStatus.CREATED)
    public LSAUser createUser(@RequestBody LSAUser user) {
        PasswordUtil pUtil = new PasswordUtil(user.getPassword());
        user.setPassword(pUtil.getEncodedPassword());
        userRepository.save(user);
        return user;
    }

    //UPDATE USER BY ID
    @PutMapping("/api/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody LSAUser user, @PathVariable Integer id) {
        if(user.getId() != id) {
            throw new IllegalArgumentException("Entered ID does not match existing user ID");
        }
        userRepository.save(user);
    }

    //DELETE USER BY ID
    @DeleteMapping("/api/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }
}
