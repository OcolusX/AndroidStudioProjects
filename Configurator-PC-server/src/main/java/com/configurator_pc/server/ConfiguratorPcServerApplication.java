package com.configurator_pc.server;

import com.configurator_pc.server.parcer.EkatalogParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class ConfiguratorPcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfiguratorPcServerApplication.class, args);

        EkatalogParser parser = new EkatalogParser();
        parser.parse();
    }

}
