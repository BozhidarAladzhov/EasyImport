package com.mytransport.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ocean_freight_prices")
public class OceanFreightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originPort;
    private String destinationPort;
    private String vehicleType;
    private double price;

    public OceanFreightEntity(){}

    public OceanFreightEntity(String originPort, String destinationPort, String vehicleType, double price) {
        this.originPort = originPort;
        this.destinationPort = destinationPort;
        this.vehicleType = vehicleType;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginPort() {
        return originPort;
    }

    public void setOriginPort(String originPort) {
        this.originPort = originPort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
