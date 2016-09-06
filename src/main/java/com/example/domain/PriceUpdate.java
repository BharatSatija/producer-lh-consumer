package com.example.domain;

import java.util.Objects;


public class PriceUpdate {

    private final String companyName;
    private final double price;

    public PriceUpdate(String companyName, double price) {
        this.companyName = companyName;
        this.price = price;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public double getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return companyName + " - " + price;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceUpdate that = (PriceUpdate) o;
        return Double.compare(that.price, price) == 0 &&
                Objects.equals(companyName, that.companyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, price);
    }
}
