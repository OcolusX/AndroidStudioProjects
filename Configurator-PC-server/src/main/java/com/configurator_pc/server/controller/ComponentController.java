package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.Component;
import com.configurator_pc.server.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ComponentController {

    @Autowired
    private ComponentRepository repository;

    @GetMapping("/components/")
    public List<Component> getAll() {
        Iterable<Component> components = repository.findAll();
        List<Component> list = new ArrayList<>();
        components.forEach(list::add);
        return list;
    }

    @GetMapping("/components/{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        Optional<Component> optionalComponent = repository.findById(id);
        if (optionalComponent.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalComponent.get(), HttpStatus.OK);
    }

    @PostMapping("/components/")
    public int add(Component component) {
        int id = repository.save(component).getId();
        return id;
    }

    @DeleteMapping("/components/")
    public void deleteAll() {
        repository.deleteAll();
    }

    @DeleteMapping("/components/{id}")
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

}