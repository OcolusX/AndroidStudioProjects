package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.ComponentOfConfiguration;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComponentOfConfigurationRepository extends CrudRepository<ComponentOfConfiguration, Integer> {
    List<ComponentOfConfiguration> findAllByConfigurationId(int id);
}
