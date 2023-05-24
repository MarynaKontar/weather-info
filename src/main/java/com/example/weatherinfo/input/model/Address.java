package com.example.weatherinfo.input.model;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable
{
    private static final long serialVersionUID = -3538660537285717526L;
    private String city;
    private String province;
    private String zip;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) && Objects.equals(province, address.province) && Objects.equals(zip, address.zip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, province, zip);
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
