package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.Currency;
import com.configurator_pc.server.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CurrencyController {

    @Autowired
    private CurrencyRepository repository;

    @GetMapping("/currency")
    public List<Currency> getAll() {
        Iterable<Currency> currencies = repository.findAll();
        List<Currency> list = new ArrayList<>();
        currencies.forEach(list::add);
        return list;
    }

    @GetMapping("/currency{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        Optional<Currency> optionalCurrency = repository.findById(id);
        if (!optionalCurrency.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalCurrency.get(), HttpStatus.OK);
    }

}
