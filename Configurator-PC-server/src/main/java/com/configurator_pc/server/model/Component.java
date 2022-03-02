package com.configurator_pc.server.model;

import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.type.TextType;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "components")
public class Component {
    @Id
    private int id;

    @Column(name = "type_id")
    private int typeId;

    private String name;


    public Component() {
    }

    public Component(int id, int typeId, String name) {
        this.id = id;
        this.typeId = typeId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return typeId == component.typeId && Objects.equals(name, component.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeId, name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
