package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.ComponentOfStore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ComponentOfStoreRepository extends CrudRepository<ComponentOfStore, Integer> {
    List<ComponentOfStore> findAllByComponentId(int componentId);
    Optional<ComponentOfStore> findByStoreIdAndComponentId(int storeId, int componentId);
}
