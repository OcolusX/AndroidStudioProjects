package com.configurator_pc.server.model;

import javax.persistence.*;

@Entity
@Table(name = "user_configurations")
public class UserConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "configuration_id")
    private int configurationId;

    @Column(name = "user_id")
    private int userId;

    public UserConfiguration() {
    }

    public UserConfiguration(int configurationId, int userId) {
        this.configurationId = configurationId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(int configurationId) {
        this.configurationId = configurationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
