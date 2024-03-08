package net.iqbalfauzan.belajarspringbootrestfulapi.service;

import jakarta.validation.Validator;
import net.iqbalfauzan.belajarspringbootrestfulapi.entity.User;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.request.RegisterUserRequest;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.request.UpdateUserRequest;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.response.UserResponse;
import net.iqbalfauzan.belajarspringbootrestfulapi.repository.UserRepository;
import net.iqbalfauzan.belajarspringbootrestfulapi.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

/**
 * Created by IqbalMF on 2024.
 * Package net.iqbalfauzan.belajarspringbootrestfulapi
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());
        userRepository.save(user);
    }

    public UserResponse get(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest updateUserRequest){
        validationService.validate(updateUserRequest);
        if (Objects.nonNull(updateUserRequest.getName())){
            user.setName(updateUserRequest.getName());
        }
        if (Objects.nonNull(updateUserRequest.getPassword())){
            user.setPassword(BCrypt.hashpw(updateUserRequest.getPassword(), BCrypt.gensalt()));
        }
        userRepository.save(user);


        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }
}
