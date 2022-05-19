package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.ComponentAttribute;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ComponentAttributeRepository extends CrudRepository<ComponentAttribute, Integer> {
    List<ComponentAttribute> findAllByComponentId(int componentId);
    Optional<ComponentAttribute> findByAttributeIdAndComponentId(int attributeId, int componentId);
}
