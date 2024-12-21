package com.sdaproject.api20216146.repository;

import com.sdaproject.api20216146.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private List<User> users = new ArrayList<>();
    private long currentId = 1L;

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(currentId++);
            users.add(user);
        } else {
            // Update existing user if found
            boolean updated = false;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(user.getId())) {
                    users.set(i, user);
                    updated = true;
                    break;
                }
            }
            // If not found by ID, treat as new
            if (!updated) {
                users.add(user);
            }
        }
        return user;
    }

    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public List<User> findAll() {
        return users;
    }

    public void deleteById(Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}
