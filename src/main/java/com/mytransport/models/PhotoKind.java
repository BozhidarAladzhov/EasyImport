package com.mytransport.models;

public enum PhotoKind {
    POL_WAREHOUSE("POL warehouse"),
    POD_WAREHOUSE("POD warehouse");

    private final String label;

    PhotoKind(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
