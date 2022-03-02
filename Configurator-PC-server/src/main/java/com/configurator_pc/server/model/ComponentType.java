package com.configurator_pc.server.model;

import javax.persistence.*;

@Entity
@Table(name = "component_types")
public class ComponentType {
    @Id
    private int id;

    private String name;

    public ComponentType() { }

    public ComponentType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
