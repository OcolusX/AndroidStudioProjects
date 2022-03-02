package com.configurator_pc.server.model;

import javax.persistence.*;

@Entity
@Table(name = "component_attributes")
public class ComponentAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "component_id")
    private int componentId;

    @Column(name = "attribute_id")
    private int attributeId;

    private String value;

    public ComponentAttribute() {
    }

    public ComponentAttribute(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
