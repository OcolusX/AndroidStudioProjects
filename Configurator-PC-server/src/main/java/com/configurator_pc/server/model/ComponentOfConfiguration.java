package com.configurator_pc.server.model;

import javax.persistence.*;

@Entity
@Table(name = "component_of_configuration")
public class ComponentOfConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "component_id")
    private int componentId;

    @Column(name = "configuration_id")
    private int configurationId;

    public ComponentOfConfiguration() {
    }

    public ComponentOfConfiguration(int componentId, int configurationId) {
        this.componentId = componentId;
        this.configurationId = configurationId;
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

    public int getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(int configurationId) {
        this.configurationId = configurationId;
    }
}
