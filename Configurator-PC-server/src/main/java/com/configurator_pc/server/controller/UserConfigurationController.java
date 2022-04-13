package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.UserConfiguration;
import com.configurator_pc.server.repository.UserConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserConfigurationController {

    @Autowired
    private UserConfigurationRepository repository;

    @GetMapping("/user_configurations")
    public List<UserConfiguration> getAll() {
        Iterable<UserConfiguration> userConfigurations = repository.findAll();
        List<UserConfiguration> list = new ArrayList<>();
        userConfigurations.forEach(list::add);
        return list;
    }

    @GetMapping("/user_configurations{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        Optional<UserConfiguration> optionalUserConfiguration = repository.findById(id);
        if (!optionalUserConfiguration.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalUserConfiguration.get(), HttpStatus.OK);
    }

    @PostMapping("/user_configurations")
    public int add(UserConfiguration userConfiguration) {
        int id = repository.save(userConfiguration).getId();
        return id;
    }

    @DeleteMapping("/user_configurations")
    public void deleteAll() {
        repository.deleteAll();
    }

    @DeleteMapping("/user_configurations{id}")
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

}
