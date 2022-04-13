package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.ComponentType;
import com.configurator_pc.server.repository.ComponentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ComponentTypeController {

    @Autowired
    private ComponentTypeRepository repository;

    @GetMapping("/component_type")
    public List<ComponentType> getAll() {
        Iterable<ComponentType> componentTypes = repository.findAll();
        List<ComponentType> list = new ArrayList<>();
        componentTypes.forEach(list::add);
        return list;
    }

    @GetMapping("/component_type{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        Optional<ComponentType> optionalComponentType = repository.findById(id);
        if (!optionalComponentType.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalComponentType.get(), HttpStatus.OK);
    }

}
