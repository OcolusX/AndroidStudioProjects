package com.example.configurator_pc.model;

import java.util.List;
import java.util.Objects;

public class Component implements Comparable<Component> {
    private final String name;
    private final ComponentType type;
    private final List<Attribute> attributeList;
    private final List<Price> priceList;

    public Component(String name, ComponentType type, List<Attribute> attributeList, List<Price> priceList) {
        this.name = name;
        this.type = type;
        this.attributeList = attributeList;
        this.priceList = priceList;

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

    @Override
    public int compareTo(Component o) {
        if(o == null) {
            throw new NullPointerException();
        }
        int compare = this.type.getId() - o.type.getId();
        if(compare != 0) {
            return compare;
        }
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return type == component.type && Objects.equals(name, component.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
