package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.Store;
import com.configurator_pc.server.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class StoreController {

    @Autowired
    private StoreRepository repository;

    @GetMapping("/stores/")
    public List<Store> getAll() {
        Iterable<Store> stores = repository.findAll();
        List<Store> list = new ArrayList<>();
        stores.forEach(list::add);
        return list;
    }

    @GetMapping("/stores/{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        Optional<Store> optionalStore = repository.findById(id);
        if (optionalStore.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalStore.get(), HttpStatus.OK);
    }

//    @PostMapping("/stores/")
//    public int add(Store store) {
//        int id = repository.save(store).getId();
//        return id;
//    }
//
//    @DeleteMapping("/stores/")
//    public void deleteAll() {
//        repository.deleteAll();
//    }
//
//    @DeleteMapping("/stores/{id}")
//    public void delete(@PathVariable int id) {
//        repository.deleteById(id);
//    }

}
