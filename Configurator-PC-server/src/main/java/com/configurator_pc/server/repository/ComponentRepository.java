package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.Component;
import org.springframework.data.repository.CrudRepository;

public interface ComponentRepository extends CrudRepository<Component, Integer> {
}
