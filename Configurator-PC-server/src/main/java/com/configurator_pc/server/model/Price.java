package com.configurator_pc.server.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "prices")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "component_of_store_id")
    private int componentOfStoreId;

    private float price;

    @Column(name = "currency_id")
    private int currencyId;

    private Date date;

    public Price() {
    }

    public Price(float price, int currencyId, Date date) {
        this.price = price;
        this.currencyId = currencyId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComponentOfStoreId() {
        return componentOfStoreId;
    }

    public void setComponentOfStoreId(int componentOfStoreId) {
        this.componentOfStoreId = componentOfStoreId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
