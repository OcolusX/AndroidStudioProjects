package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.ComponentOfStore;
import com.configurator_pc.server.repository.ComponentOfStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ComponentOfStoreController {

    @Autowired
    private ComponentOfStoreRepository repository;

    @GetMapping("/component_of_store/")
    public List<ComponentOfStore> getAll() {
        Iterable<ComponentOfStore> componentOfStores = repository.findAll();
        List<ComponentOfStore> list = new ArrayList<>();
        componentOfStores.forEach(list::add);
        return list;
    }

    @GetMapping("/component_of_store/{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        Optional<ComponentOfStore> optionalComponentOfStore = repository.findById(id);
        if (optionalComponentOfStore.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalComponentOfStore.get(), HttpStatus.OK);
    }

//    @PostMapping("/component_of_store/")
//    public int add(ComponentOfStore componentOfStore) {
//        int id = repository.save(componentOfStore).getId();
//        return id;
//    }
//
//    @DeleteMapping("/component_of_store/")
//    public void deleteAll() {
//        repository.deleteAll();
//    }
//
//    @DeleteMapping("/component_of_store/{id}")
//    public void delete(@PathVariable int id) {
//        repository.deleteById(id);
//    }

}
