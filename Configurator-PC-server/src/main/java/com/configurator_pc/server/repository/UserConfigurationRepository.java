package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.UserConfiguration;
import org.springframework.data.repository.CrudRepository;

public interface UserConfigurationRepository extends CrudRepository<UserConfiguration, Integer> {
    void findAllByConfigurationId(int id);
}
