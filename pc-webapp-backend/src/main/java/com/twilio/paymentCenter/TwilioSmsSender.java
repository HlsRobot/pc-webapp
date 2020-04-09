package com.twilio.paymentCenter;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("twilio")
public class TwilioSmsSender implements SmsSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioSmsSender.class);

    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public TwilioSmsSender(final TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public void sendSms(final SmsRequest smsRequest) {
        if (isPhoneNumberValid(smsRequest.getPhoneNumber())) {
            PhoneNumber to = new PhoneNumber(smsRequest.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(this.twilioConfiguration.getTrialNumber());
            String message = smsRequest.getMessage();
            MessageCreator creator = Message.creator(to, from, message);
            creator.create();
            LOGGER.info("Send sms {}", smsRequest);
        } else {
            throw new IllegalArgumentException(
                    "Phone number [" + smsRequest.getPhoneNumber() + "] is not a valid number"
            );
        }
    }

    @Override
    public void sendReceipt(final Payment payment) {
        final PhoneNumber to = new PhoneNumber(payment.getCalledNumber());
        final PhoneNumber from = new PhoneNumber(this.twilioConfiguration.getAlpha());

        final String message = String.format("Your personal receipt: OrderId: %d,  Amount: %s€,  Method: %s,  Card Number: %s,  Card Owner: %s",
                payment.getId(), payment.getAmount(), payment.getMethod(), payment.getCreditCardNumber(), payment.getOwner());
        final MessageCreator creator = Message.creator(to, from, message);
        creator.create();
    }

    private boolean isPhoneNumberValid(final String phoneNumber) {
        // TODO: Implement phone number validator
        return true;
    }
}
