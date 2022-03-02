package com.configurator_pc.server.model;

import javax.persistence.*;

@Entity
@Table(name = "component_of_store")
public class ComponentOfStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "component_id")
    private int componentId;

    @Column(name = "store_id")
    private int storeId;

    private String url;


    public ComponentOfStore() {
    }

    public ComponentOfStore(String url) {
        this.url = url;
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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
