package com.mytransport.models;

public class DomesticRotterdam {

    private String originPort;
    private String destinationPort;
    private String vehicleType;
    private double price;

    public DomesticRotterdam(String originPort, String destinationPort, String vehicleType, double price) {
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
