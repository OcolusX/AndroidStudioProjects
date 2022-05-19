package com.configurator_pc.server.controller;

import com.configurator_pc.server.controller.request_body.UserRequestBody;
import com.configurator_pc.server.model.User;
import com.configurator_pc.server.repository.UserRepository;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/users")
    public List<User> getAll() {
        Iterable<User> users = repository.findAll();
        List<User> list = new ArrayList<>();
        users.forEach(list::add);
        return list;
    }

    @GetMapping("/users{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/users", consumes = {"application/json"})
    public ResponseEntity<Object> saveUser(@RequestBody UserRequestBody userRequestBody) {
        int id = userRequestBody.getId();
        Optional<User> byId = repository.findById(id);
        if(byId.isPresent()) {
            User user = byId.get();
            user.setName(userRequestBody.getName());
            repository.save(user);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else {
            id = repository.save(new User(userRequestBody.getName())).getId();
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }
    }


}
