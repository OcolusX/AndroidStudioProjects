package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.AttributeType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AttributeTypeRepository extends CrudRepository<AttributeType, Integer> {
    Optional<AttributeType> findByName(String name);
}
