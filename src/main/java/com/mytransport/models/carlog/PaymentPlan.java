package com.mytransport.models.carlog;

public enum PaymentPlan {
    SINGLE("1 вноска"),
    FOUR_INSTALLMENTS("4 вноски");

    private final String label;

    PaymentPlan(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
