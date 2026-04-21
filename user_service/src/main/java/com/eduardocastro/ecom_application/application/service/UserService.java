package com.eduardocastro.ecom_application.application.service;

import com.eduardocastro.ecom_application.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private List<User> users = new ArrayList<User>();

    public List<User> list() {
        return users;
    }

    public User create(User user) {
        User newUser = User.create(user.getFirstName(), user.getLastName());
        users.add(newUser);
        return newUser;
    }

    public Optional<User> getById(UUID id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public boolean update(UUID id, User user) {
       return users.stream()
               .filter(u -> u.getId().equals(id))
               .findFirst()
               .map(existingUser -> {
                  existingUser.update(user.getFirstName(), user.getLastName());
                  return true;
               }).orElse(false);
    }
}
