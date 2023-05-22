package com.example.weatherinfo.output.entity;

import com.example.weatherinfo.input.model.Address;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@CsvRecord(separator = ",")
@Entity
@Table
public class Weather
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DataField(pos = 1)
    private String additionalInfo;
    @DataField(pos = 2, pattern = "WindDirection.class", trim = true)
    private WindDirection windDirection;
    @DataField(pos = 3)
    private String city;
    @DataField(pos = 4)
    private String province;
    @DataField(pos = 5)
    private String zip;

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WindDirection getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(WindDirection windDirection) {
        this.windDirection = windDirection;
    }

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

    public Address getAddress() {
        Address address = new Address();
        address.setCity(city);
        address.setProvince(province);
        address.setZip(zip);
        return address;
//        return "Address{" +
//                "houseNumber='" + houseNumber + '\'' +
//                ", city='" + city + '\'' +
//                ", province='" + province + '\'' +
//                ", zip='" + zip + '\'' +
//                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return Objects.equals(id, weather.id) && Objects.equals(additionalInfo, weather.additionalInfo) && Objects.equals(windDirection, weather.windDirection) && Objects.equals(city, weather.city) && Objects.equals(province, weather.province) && Objects.equals(zip, weather.zip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, additionalInfo, windDirection, city, province, zip);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
