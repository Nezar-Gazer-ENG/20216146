package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private List<User> users = new ArrayList<>();
    private long currentId = 1L;

    public User createUser(User user) {
        user.setId(currentId++);
        users.add(user);
        return user;
    }

    public User getUser(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User updateUser(Long id, User updatedUser) {
        for (User existingUser : users) {
            if (existingUser.getId().equals(id)) {
                existingUser.setName(updatedUser.getName());
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
                existingUser.setPassword(updatedUser.getPassword());
                return existingUser;
            }
        }
        return null;
    }

    public String deleteUser(Long id) {
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        return removed ? "User deleted" : "User not found";
    }
}
