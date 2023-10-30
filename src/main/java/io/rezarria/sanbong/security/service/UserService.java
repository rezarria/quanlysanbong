package io.rezarria.sanbong.security.service;

import io.rezarria.sanbong.model.User;
import io.rezarria.sanbong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void create(User user) {
        userRepository.save(user);
    }
}
