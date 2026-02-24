package com.mytransport.models;

public enum VehicleStatus {
    IN_AUCTION("In auction"),
    ON_THE_WAY_TO_USA_WAREHOUSE("On the way to USA warehouse"),
    IN_USA_WAREHOUSE("In USA warehouse"),
    PLANNED_TO_LOAD_IN_CONTAINER("Planned to load in container"),
    LOADED_IN_CONTAINER("Loaded in container"),
    SHIPPING_TO_POD("Shipping to POD"),
    UNLOADED_FROM_VESSEL("Unloaded from the vessel"),
    UNLOADED_FROM_CONTAINER("Unloaded from the container"),
    WAITING_FOR_CUSTOMS_CLEARANCE("Waiting for customs clearance"),
    CUSTOMS_CLEARED("Customs cleared"),
    WAITING_FOR_DELIVERY_POD_SOFIA("Waiting for delivery POD - Sofia"),
    ON_THE_WAY_TO_SOFIA("On the way to Sofia"),
    ARRIVED_IN_SOFIA_WAREHOUSE("Arrived in Sofia warehouse");

    private final String label;

    VehicleStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
