package com.twilio.paymentCenter.jlpsolution.enums;

public enum TradeEnum {
    ELECTRICIAN("1"), LOCKSMITH("2");

    private final String id;

    TradeEnum(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static TradeEnum getById(final String id) {
        for (final TradeEnum contractPartner : TradeEnum.values()) {
            if (contractPartner.getId().equals(id)) {
                return contractPartner;
            }
        }
        return ELECTRICIAN;
    }
}
