package com.example.configurator_pc.model;

import java.util.ArrayList;
import java.util.List;

public class Component {
    private final String name;
    private final ComponentType type;
    private final List<Attribute> attributeList;
    private final List<Price> priceList;

    public Component(String name, ComponentType type, List<Attribute> attributeList, List<Price> priceList) {
        this.name = name;
        this.type = type;
        this.attributeList = new ArrayList<>(attributeList);
        this.priceList = new ArrayList<>(priceList);
    }

    public String getName() {
        return name;
    }

    public ComponentType getType() {
        return type;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public List<Price> getPriceList() {
        return priceList;
    }
}
