package com.example.configurator_pc.model;

import java.util.Date;

public class Price {
    private final float price;
    private final Currency currency;
    private final String storeName;
    private final String url;
    private final Date date;

    public Price(float price, Currency currency, String storeName, String url, Date date) {
        this.price = price;
        this.currency = currency;
        this.storeName = storeName;
        this.url = url;
        this.date = date;
    }

    public float getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getUrl() {
        return url;
    }

    public Date getDate() {
        return date;
    }
}
