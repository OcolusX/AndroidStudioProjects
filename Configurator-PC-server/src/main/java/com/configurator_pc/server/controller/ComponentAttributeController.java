package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.ComponentAttribute;
import com.configurator_pc.server.repository.ComponentAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ComponentAttributeController {

    @Autowired
    private ComponentAttributeRepository repository;

    @GetMapping("/component_attributes/")
    public List<ComponentAttribute> getAll() {
        Iterable<ComponentAttribute> componentAttributes = repository.findAll();
        List<ComponentAttribute> list = new ArrayList<>();
        componentAttributes.forEach(list::add);
        return list;
    }

    @GetMapping("/component_attributes/{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        Optional<ComponentAttribute> optionalComponentAttribute = repository.findById(id);
        if (optionalComponentAttribute.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalComponentAttribute.get(), HttpStatus.OK);
    }

//    @PostMapping("/component_attributes/")
//    public int add(ComponentAttribute componentAttribute) {
//        int id = repository.save(componentAttribute).getId();
//        return id;
//    }
//
//    @DeleteMapping("/component_attributes/")
//    public void deleteAll() {
//        repository.deleteAll();
//    }
//
//    @DeleteMapping("/component_attributes/{id}")
//    public void delete(@PathVariable int id) {
//        repository.deleteById(id);
//    }
}