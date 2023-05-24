package com.example.weatherinfo.output.entity;

import com.example.weatherinfo.input.model.Address;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table
@NamedQueries({@NamedQuery(name="OutBoundWeatherInfo_fetchAll", query = "Select x from OutBoundWeatherInfo x")})
public class OutBoundWeatherInfo implements Serializable {

    private static final long serialVersionUID = -57028997632470799L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String additionalInfo;
    private WindDirection windDirection;
    @Embedded
    private Address address;

    public OutBoundWeatherInfo() {
    }

    public OutBoundWeatherInfo(String additionalInfo, WindDirection windDirection, Address address) {
        this.additionalInfo = additionalInfo;
        this.windDirection = windDirection;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public WindDirection getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(WindDirection windDirection) {
        this.windDirection = windDirection;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "OutBoundWeatherInfo{" +
                "id=" + id +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", windDirection=" + windDirection +
                ", address=" + address +
                '}';
    }
}


