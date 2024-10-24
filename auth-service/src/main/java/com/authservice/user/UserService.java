package com.authservice.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User createUser(User user) {
        if(userDao.existsByEmail(user.getEmail()))
            throw new RuntimeException("User is already exist with this email - "+user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.save(user);
    }

    public List<User> getUserList() {
        return userDao.findAll();
    }

    public User getUserById(int id) {
        return userDao.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
