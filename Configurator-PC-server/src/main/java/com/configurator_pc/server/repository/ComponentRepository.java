package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.Component;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ComponentRepository extends CrudRepository<Component, Integer> {
    List<Component> findAllByTypeIdOrderById(int typeId);
    long count();
    long countAllByTypeId(int typeId);
}
