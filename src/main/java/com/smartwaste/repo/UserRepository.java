package com.smartwaste.repo;

import com.smartwaste.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
}
