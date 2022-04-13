package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.ComponentOfStore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ComponentOfStoreRepository extends CrudRepository<ComponentOfStore, Integer> {
    List<ComponentOfStore> findAllByComponentId(int componentId);
}
