package com.mytransport.models;

public class TransportPrice {

    private String originPort;
    private String destinationPort;
    private String vehicleType;
    private double price;

    public TransportPrice(String originPort, String destinationPort, String vehicleType, double price) {
        this.originPort = originPort;
        this.destinationPort = destinationPort;
        this.vehicleType = vehicleType;
        this.price = price;
    }

    public String getOriginPort() {
        return originPort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public double getPrice() {
        return price;
    }
}
