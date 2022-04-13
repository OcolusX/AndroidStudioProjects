package com.example.configurator_pc.model;

import java.util.List;
import java.util.TreeSet;

public class Configuration {
    private final int id;
    private final String name;
    private final User creator;
    private final List<Component> componentList;

    public Configuration(int id, String name, User creator, List<Component> componentList) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.componentList = componentList;
    }

    // Возвращает минимальную и максимальную суммы сборки через дефис
    public String getTotalPrice() {
        float minTotalPrice = 0f;
        float maxTotalPrice = 0f;
        for(Component component : componentList) {
            TreeSet<Price> priceList = new TreeSet<>(component.getPriceList());
            minTotalPrice += priceList.first().getPrice();
            maxTotalPrice += priceList.last().getPrice();
        }
        return minTotalPrice + "-" + maxTotalPrice;
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
