package com.ncnl.quiz_service.controller;

import com.ncnl.quiz_service.model.Progress;
import com.ncnl.quiz_service.model.User;
import com.ncnl.quiz_service.model.UserInput;
import com.ncnl.quiz_service.repository.ProgressRepository;
import com.ncnl.quiz_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @QueryMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @QueryMapping
    public User getUserById(@Argument Integer id){
        log.info("Finding user by id {}", id);

        return userRepository.getReferenceById(id);
    }

    @MutationMapping
    public User createUser(@Argument UserInput userInput){
        var found = userRepository.findBySrcode(userInput.srcode());

        if (found != null){
            throw new IllegalArgumentException("There is already a person with the srcode");
        }

        var password = passwordEncoder.encode(userInput.password());

        User user = new User();
        user.setSrcode(userInput.srcode());
        user.setFullName(userInput.fullName());
        user.setPassword(password); //hashed password
        user.setRole("USER");
        user.setProgress(userInput.progress());

        return userRepository.save(user);
    }

    @MutationMapping
    public User login(@Argument String srcode, @Argument String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(srcode, password)
            );
            return userRepository.findBySrcode(srcode);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Boolean deleteUserById(@Argument Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No user found"));

        userRepository.deleteById(user.getId());

        return true;
    }

    @MutationMapping
    public User updateUser(@Argument Integer id,@Argument UserInput input){

        var found = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));


        var password = BCrypt.hashpw(input.password(), BCrypt.gensalt());


        found.setSrcode(input.srcode());
        found.setFullName(input.fullName());
        found.setPassword(password);

        return userRepository.save(found);
    }

    @BatchMapping
    public List<List<Progress>> progress(List<User> users){
        log.info("Batch Mapping of progress for {} users", users.size());

        List<Integer> userId = users.stream()
                .map(User::getId)
                .toList();

        List<Progress> allProgress = progressRepository.findProgressByUserIdIn(userId);

        Map<Integer, List<Progress>> progressByUserId = allProgress.stream()
                .collect(Collectors.groupingBy(progress -> progress.getUser().getId()));

        return users.stream()
                .map(user -> progressByUserId.getOrDefault(user.getId(), Collections.emptyList()))
                .toList();
    }

}
