package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.User;
import com.configurator_pc.server.repository.UserRepository;
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
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
    }

    @PostMapping("/users")
    public int add(User user) {
        int id = repository.save(user).getId();
        return id;
    }

    @DeleteMapping("/users")
    public void deleteAll() {
        repository.deleteAll();
    }

    @DeleteMapping("/users{id}")
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

}
