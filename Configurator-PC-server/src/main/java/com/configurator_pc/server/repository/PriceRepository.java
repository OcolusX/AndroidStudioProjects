package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.Price;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PriceRepository extends CrudRepository<Price, Integer> {
    Price findFirstByComponentOfStoreIdOrderByDate(int componentOfStoreId);
}
