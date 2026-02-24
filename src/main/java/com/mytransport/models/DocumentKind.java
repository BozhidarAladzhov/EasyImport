package com.mytransport.models;

public enum DocumentKind {
    CUSTOMS("Customs"),
    INVOICE("Invoice"),
    OTHER("Other");

    private final String label;

    DocumentKind(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
