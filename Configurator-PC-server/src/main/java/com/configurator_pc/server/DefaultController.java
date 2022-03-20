package com.configurator_pc.server;

import com.configurator_pc.server.model.Component;
import com.configurator_pc.server.parser.Parser;
import com.configurator_pc.server.parser.ParserThreadPool;
import com.configurator_pc.server.parser.hardprice.HardpriceParser;
import com.configurator_pc.server.parser.hardpriceAPI.HardpriceAPIParser;
import com.configurator_pc.server.repository.ComponentRepository;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DefaultController {

    @Autowired
    private ComponentRepository repository;

    @RequestMapping("/")
    public String index(Model model) {
        Iterable<Component> componentIterable = repository.findAll();
        List<Component> componentList = new ArrayList<>();
        componentIterable.forEach(componentList::add);
        model.addAttribute("count", componentList.size());

        return "index";
    }

    @PostMapping("/parse")
    public String parse(@ModelAttribute("password") String password, Model model) {
        if (password.equals("12345")) {
            // TODO: Реализовать корректный запуск парсера;

            new Thread(() -> {
                Parser parser = new HardpriceAPIParser();
                parser.parse();
            }).start();
        }
        return "parse";
    }
}
