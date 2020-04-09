package com.twilio.paymentCenter;

public interface SmsSender {

    void sendSms(SmsRequest smsRequest);

    void sendReceipt(Payment payment);

    // or maybe void sendSms(String phoneNumber, String message);
}
