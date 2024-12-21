package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.User;
import com.sdaproject.api20216146.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.usernameExists(user.getUsername())) {
            throw new RuntimeException("Username is already taken.");
        }
        if (userRepository.emailExists(user.getEmail())) {
            throw new RuntimeException("Email is already registered.");
        }
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        User existing = getUser(id);
        if (existing != null) {
            if (!existing.getUsername().equalsIgnoreCase(updatedUser.getUsername()) &&
                userRepository.usernameExists(updatedUser.getUsername())) {
                throw new RuntimeException("Username is already taken.");
            }
            if (!existing.getEmail().equalsIgnoreCase(updatedUser.getEmail()) &&
                userRepository.emailExists(updatedUser.getEmail())) {
                throw new RuntimeException("Email is already registered.");
            }

            existing.setName(updatedUser.getName());
            existing.setUsername(updatedUser.getUsername());
            existing.setEmail(updatedUser.getEmail());
            existing.setPhoneNumber(updatedUser.getPhoneNumber());
            existing.setPassword(updatedUser.getPassword());
            return userRepository.save(existing);
        }
        return null;
    }

    public String deleteUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            userRepository.deleteById(id);
            return "User deleted";
        }
        return "User not found";
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean usernameExists(String username) {
        return userRepository.usernameExists(username);
    }

    public boolean emailExists(String email) {
        return userRepository.emailExists(email);
    }

    public User findByEmail(String email) {
        return userRepository.findAll().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
