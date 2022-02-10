package com.kocurek.loans.service;

import com.kocurek.loans.domain.User;
import com.kocurek.loans.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElse(new User());
        return user;
    }
    public User getUserByLogin(String login) {
        User user = userRepository.findByLogin(login);
                //orElseThrow(EntityNotFoundException::new);
        return user;
    }
}
