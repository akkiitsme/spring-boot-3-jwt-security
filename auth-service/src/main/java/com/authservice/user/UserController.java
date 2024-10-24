package com.authservice.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){
        User user = userService.createUser(modelMapper.map(userDTO,User.class));
        return ResponseEntity.ok(modelMapper.map(user,UserDTO.class));
    }

    @PreAuthorize("hasAuthority('SuperAdmin')")
    @GetMapping("/user-list")
    public ResponseEntity<?> getAllUserList(){
        return ResponseEntity.ok(userService.getUserList());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id){
        return ResponseEntity.ok(modelMapper.map(userService.getUserById(id),UserDTO.class));
    }


}
