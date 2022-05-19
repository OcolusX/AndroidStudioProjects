package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.ComponentType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentTypeRepository extends CrudRepository<ComponentType, Integer> {
}
