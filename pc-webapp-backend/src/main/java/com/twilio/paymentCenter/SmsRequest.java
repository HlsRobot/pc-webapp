package com.twilio.paymentCenter;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class SmsRequest {

    @NotBlank
    private final String phoneNumber; // destination

    @NotBlank
    private final String message;

    public SmsRequest(@JsonProperty("phoneNumber") final String phoneNumber,
                      @JsonProperty("message") final String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "SmsRequest{" +
                "phoneNumber= ..." + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
