package com.example.configurator_pc.model;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private final int id;
    private final String name;
    private final User creator;
    private final List<Component> componentList;

    public Configuration(int id, String name, User creator, List<Component> componentList) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.componentList = new ArrayList<>(componentList);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getCreator() {
        return creator;
    }

    public List<Component> getComponentList() {
        return componentList;
    }
}
