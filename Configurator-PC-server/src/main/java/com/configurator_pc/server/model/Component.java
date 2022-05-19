package com.configurator_pc.server.model;

import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.type.TextType;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "components")
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type_id")
    private int typeId;

    private String name;

    private String image;

    private String description;


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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Component() {
    }

    public Component(int typeId, String name, String image, String description) {
        this.typeId = typeId;
        this.name = name;
        this.image = image;
        this.description = description;
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
}
