package com.configurator_pc.server.repository;

import com.configurator_pc.server.model.Currency;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {
}
