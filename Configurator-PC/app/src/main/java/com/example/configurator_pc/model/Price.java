package com.example.configurator_pc.model;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;
import java.util.TreeSet;

public class Price implements Comparable<Price>  {
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

    @Override
    public int compareTo(Price o) {
        if(o == null) {
            throw new NullPointerException();
        }
        if(this == o) {
            return 0;
        }
        int compare = Float.compare(this.price, o.price);
        if (compare != 0) {
            return compare;
        }
        return this.storeName.compareTo(o.storeName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Float.compare(price.price, this.price) == 0 && Objects.equals(url, price.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, url);
    }
}
