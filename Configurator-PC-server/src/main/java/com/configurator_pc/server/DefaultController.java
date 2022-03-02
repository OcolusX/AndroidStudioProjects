package com.configurator_pc.server;

import com.configurator_pc.server.model.Component;
import com.configurator_pc.server.parcer.EkatalogParser;
import com.configurator_pc.server.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
