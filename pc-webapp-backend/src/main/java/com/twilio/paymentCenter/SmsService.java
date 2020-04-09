package com.twilio.paymentCenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@org.springframework.stereotype.Service
public class SmsService {

    private final SmsSender smsSender;

    @Autowired
    public SmsService(@Qualifier("twilio") final TwilioSmsSender smsSender) {
        this.smsSender = smsSender;
    }

    public void sendSms(final SmsRequest smsRequest) {
        this.smsSender.sendSms(smsRequest);
    }

    public void sendReceipt(final Payment payment) {
        this.smsSender.sendReceipt(payment);
    }
}
