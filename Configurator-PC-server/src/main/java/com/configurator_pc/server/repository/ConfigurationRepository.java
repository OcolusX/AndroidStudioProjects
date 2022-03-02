package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.Configuration;
import org.springframework.data.repository.CrudRepository;

public interface ConfigurationRepository extends CrudRepository<Configuration, Integer> {
}
