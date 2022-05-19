package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.Store;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StoreRepository extends CrudRepository<Store, Integer> {
    Optional<Store> findByName(String name);
}
