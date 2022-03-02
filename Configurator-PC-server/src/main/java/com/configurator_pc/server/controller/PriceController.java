package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.Price;
import com.configurator_pc.server.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PriceController {

    @Autowired
    private PriceRepository repository;

    @GetMapping("/prices/")
    public List<Price> getAll() {
        Iterable<Price> prices = repository.findAll();
        List<Price> list = new ArrayList<>();
        prices.forEach(list::add);
        return list;
    }

    @GetMapping("/prices/{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        Optional<Price> optionalPrice = repository.findById(id);
        if (optionalPrice.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalPrice.get(), HttpStatus.OK);
    }

//    @PostMapping("/prices/")
//    public int add(Price price) {
//        int id = repository.save(price).getId();
//        return id;
//    }
//
//    @DeleteMapping("/prices/")
//    public void deleteAll() {
//        repository.deleteAll();
//    }
//
//    @DeleteMapping("/prices/{id}")
//    public void delete(@PathVariable int id) {
//        repository.deleteById(id);
//    }

}
