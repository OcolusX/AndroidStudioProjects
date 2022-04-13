package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.Configuration;
import com.configurator_pc.server.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ConfigurationController {
    @Autowired
    private ConfigurationRepository repository;

    @GetMapping("/configurations")
    public List<Configuration> getAll() {
        Iterable<Configuration> configurations = repository.findAll();
        List<Configuration> list = new ArrayList<>();
        configurations.forEach(list::add);
        return list;
    }

    @GetMapping("/configurations{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        Optional<Configuration> optionalConfiguration = repository.findById(id);
        if (!optionalConfiguration.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalConfiguration.get(), HttpStatus.OK);
    }

    @PostMapping("/configurations")
    public int add(Configuration configuration) {
        int id = repository.save(configuration).getId();
        return id;
    }

    @DeleteMapping("/configurations")
    public void deleteAll() {
        repository.deleteAll();
    }

    @DeleteMapping("/configurations{id}")
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

}
