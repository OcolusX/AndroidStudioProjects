package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
