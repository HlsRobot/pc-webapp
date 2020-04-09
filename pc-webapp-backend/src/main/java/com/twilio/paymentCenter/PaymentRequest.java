package com.twilio.paymentCenter;

public class PaymentRequest {

    private String phoneNumber;
    private String amount;

    public PaymentRequest(final String phoneNumber, final String amount) {
        this.phoneNumber = phoneNumber;
        this.amount = amount;
    }

    public PaymentRequest() {
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }
}
