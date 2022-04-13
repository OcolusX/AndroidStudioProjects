package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.AttributeType;
import com.configurator_pc.server.repository.AttributeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AttributeTypeController {

    @Autowired
    private AttributeTypeRepository repository;

    @GetMapping("/attribute_types")
    public List<AttributeType> getAll() {
        Iterable<AttributeType> attributeTypes = repository.findAll();
        List<AttributeType> list = new ArrayList<>();
        attributeTypes.forEach(list::add);
        return list;
    }

    @GetMapping(value = "/attribute_types", params = "id")
    public ResponseEntity<Object> getById(@RequestParam(value = "id") int id) {
        Optional<AttributeType> optionalAttributeType = repository.findById(id);
        if (!optionalAttributeType.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalAttributeType.get(), HttpStatus.OK);
    }

}