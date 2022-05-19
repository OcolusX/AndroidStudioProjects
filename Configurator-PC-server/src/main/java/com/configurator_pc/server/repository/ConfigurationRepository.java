package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.Configuration;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConfigurationRepository extends CrudRepository<Configuration, Integer> {
    List<Configuration> findAllByCreatorId(int id);
    void deleteAllByCreatorId(int id);
}
