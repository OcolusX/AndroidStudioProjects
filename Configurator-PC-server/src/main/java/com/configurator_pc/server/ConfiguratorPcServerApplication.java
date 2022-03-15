package com.configurator_pc.server;

import com.configurator_pc.server.parser.Parser;
import com.configurator_pc.server.parser.hardprice.HardpriceParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConfiguratorPcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfiguratorPcServerApplication.class, args);
    }

}
