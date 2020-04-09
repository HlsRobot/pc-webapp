package com.twilio.paymentCenter.jlpsolution.enums;

public enum JobEnum {
    SOCKETS("1", TradeEnum.ELECTRICIAN), LIGHTS("2", TradeEnum.ELECTRICIAN),
    LOCKS("1", TradeEnum.LOCKSMITH), SMARTLOCKS("2", TradeEnum.LOCKSMITH);

    private final String id;

    private final TradeEnum trade;

    JobEnum(String id, TradeEnum trade) {
        this.id = id;
        this.trade = trade;
    }

    public String getId() {
        return id;
    }

    public TradeEnum getTrade() {
        return trade;
    }

    public static JobEnum getByTradeAndId(final TradeEnum trade, final String id) {
        for (final JobEnum job : JobEnum.values()) {
            if (job.getId().equals(id) && job.getTrade().equals(trade)) {
                return job;
            }
        }
        return SOCKETS;
    }
}
