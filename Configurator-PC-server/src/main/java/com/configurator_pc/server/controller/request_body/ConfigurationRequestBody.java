package com.configurator_pc.server.controller.request_body;

import java.util.List;

public class ConfigurationRequestBody {
    private int id;
    private int creatorId;
    private String name;
    private List<Integer> componentIdList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getComponentIdList() {
        return componentIdList;
    }

    public void setComponentIdList(List<Integer> componentIdList) {
        this.componentIdList = componentIdList;
    }
}
