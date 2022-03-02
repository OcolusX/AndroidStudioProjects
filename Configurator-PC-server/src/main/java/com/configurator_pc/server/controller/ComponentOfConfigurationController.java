package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.ComponentOfConfiguration;
import com.configurator_pc.server.repository.ComponentOfConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ComponentOfConfigurationController {

    @Autowired
    private ComponentOfConfigurationRepository repository;

    @GetMapping("/component_of_configuration/")
    public List<ComponentOfConfiguration> getAll() {
        Iterable<ComponentOfConfiguration> componentOfConfigurations = repository.findAll();
        List<ComponentOfConfiguration> list = new ArrayList<>();
        componentOfConfigurations.forEach(list::add);
        return list;
    }

    @GetMapping("/component_of_configuration/{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        Optional<ComponentOfConfiguration> optionalComponentOfConfiguration = repository.findById(id);
        if (optionalComponentOfConfiguration.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalComponentOfConfiguration.get(), HttpStatus.OK);
    }

//    @PostMapping("/component_of_configuration/")
//    public int add(ComponentOfConfiguration componentOfConfiguration) {
//        int id = repository.save(componentOfConfiguration).getId();
//        return id;
//    }
//
//    @DeleteMapping("/component_of_configuration/")
//    public void deleteAll() {
//        repository.deleteAll();
//    }
//
//    @DeleteMapping("/component_of_configuration/{id}")
//    public void delete(@PathVariable int id) {
//        repository.deleteById(id);
//    }

}