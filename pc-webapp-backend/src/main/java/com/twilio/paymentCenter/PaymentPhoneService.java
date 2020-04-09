package com.twilio.paymentCenter;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Pay;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PaymentPhoneService {

    private final TwilioConfiguration twilioConfiguration;

    private final PaymentRepository paymentRepository;

    private final SmsService smsService;

    @Autowired
    public PaymentPhoneService(final TwilioConfiguration twilioConfiguration, final PaymentRepository paymentRepository,
                               final SmsService smsService) {
        this.twilioConfiguration = twilioConfiguration;
        this.paymentRepository = paymentRepository;
        this.smsService = smsService;
    }

    public String makeAPaymentCall(final PaymentRequest paymentRequest) throws URISyntaxException {
        Twilio.init(this.twilioConfiguration.getAccountSid(), this.twilioConfiguration.getAuthToken());

    //        final URI uri = new URI("https://twilio-example-app.herokuapp.com/api/voice");
        final URI uri = new URI(this.twilioConfiguration.getUrl() + "voice");

        final Call call = Call.creator(
                new PhoneNumber(paymentRequest.getPhoneNumber()), new PhoneNumber(this.twilioConfiguration.getTrialNumber()), uri)
                .create();

        final Payment payment = new Payment(call.getSid(), paymentRequest.getPhoneNumber(), paymentRequest.getAmount(),
                "credit card");
        this.paymentRepository.save(payment);
        System.out.println(String.format("Initializing a payment call with sid: %s",call.getSid()));
        return call.getSid();
    }

    public VoiceResponse getCorrectVoiceResponse(final String callSid) {

        // Create a TwiML response and add our friendly message.
        final VoiceResponse.Builder builder = new VoiceResponse.Builder();

        final Payment payment = this.paymentRepository.findByCallSid(callSid).orElse(new Payment());

        if (payment.getCallSid() == null || payment.getAmount() == null) {
            return this.sendErrorMessage();
        }

        if (payment.getCreditCardNumber() == null) {
            return this.sendInitialMessage(payment);
        }

        return builder.build();
    }

    private VoiceResponse sendErrorMessage() {
        return new VoiceResponse.Builder().say(new Say.Builder("There is no active transaction currently").build()).build();
    }

    private VoiceResponse sendInitialMessage(final Payment payment) {
        final Say say  = new Say.Builder(
                String.format("This is the automated payment system of " + this.twilioConfiguration.getCompanyName() +
                        "There is a payment request associated to your account for the amount of %s euros. " +
                        "Your selected method is %s. " +
                        "Please type in the 16 Digits of your credit card.", payment.getAmount(), payment.getMethod()))
                .build();
        final Gather gather = new Gather.Builder().numDigits(16).say(say).action("/api/credit").build();
        return new VoiceResponse.Builder().gather(gather).build();
    }

    public VoiceResponse handleCreditCardNumber(final String creditCard, final String callSid) {

        //TODO validate credit card number

        if (creditCard != null) {
            final Payment payment = this.paymentRepository.findByCallSid(callSid).orElse(new Payment());
            final String sanitizedCreditCard = "XXXX-XXXX-XXXX-" + creditCard.substring(12);
            payment.setCreditCardNumber(sanitizedCreditCard);
            this.paymentRepository.save(payment);
        }

        final Say say = new Say.Builder("Thank you for providing your credit card number. " +
                "Please tell us the name of the credit card owner as written on the card.").build();
        final Gather gather = new Gather.Builder().inputs(Gather.Input.SPEECH).language(Gather.Language.EN_US)
                .actionOnEmptyResult(true).speechTimeout("3").say(say).action("/api/owner").build();
        return new VoiceResponse.Builder().gather(gather).build();
    }

    public VoiceResponse handleOwner(final String owner, final String callSid) {

        if (owner != null) {
            final Payment payment = this.paymentRepository.findByCallSid(callSid).orElse(new Payment());
            payment.setOwner(owner);
            this.paymentRepository.save(payment);
        } else {
            final Say say = new Say.Builder("Sorry I did not get that. Please repeat the name of the credit card owner as written on the card.").build();
            final Gather gather = new Gather.Builder().inputs(Gather.Input.SPEECH).language(Gather.Language.EN_GB)
                    .actionOnEmptyResult(true).speechTimeout("3").say(say).action("/api/owner").build();
            return new VoiceResponse.Builder().gather(gather).build();
        }

        final Say say = new Say.Builder("Please type the expiration date of the credit card in the format of 4 digits. " +
                "For May of 2020 type 0, 5, 2, 0.").build();
        final Gather gather = new Gather.Builder().numDigits(4).say(say).action("/api/date").build();
        return new VoiceResponse.Builder().gather(gather).build();
    }

    public VoiceResponse handleDate(final String date, final String callSid) {

        //TODO validate date

        if (date != null) {
            final String sanitizeDate = date.substring(0,2) + "/" + date.substring(2);
            final Payment payment = this.paymentRepository.findByCallSid(callSid).orElse(new Payment());
            payment.setExpirationDate(sanitizeDate);
            this.paymentRepository.save(payment);
        }

        final Say say = new Say.Builder("Please type in the security code of the credit card. " +
                "You can find the 3 digit number on the back of the card.").build();
        final Gather gather = new Gather.Builder().numDigits(3).say(say).action("/api/finish").build();
        return new VoiceResponse.Builder().gather(gather).build();
    }

    public VoiceResponse handleSecurityNumber(final String securityNumber, final String callSid) {

        //TODO validate security number

        final Payment payment = this.paymentRepository.findByCallSid(callSid).orElse(new Payment());
        if (securityNumber != null) {
            payment.setSecurityNumber("XXX");
            payment.setSuccess(true);
            this.paymentRepository.save(payment);
        }

        final Say say = new Say.Builder("You have successfully submitted your credit card data. " +
                "You will receive shortly an SMS with the receipt. Thank you for using our services.").build();

        this.smsService.sendReceipt(payment);
        final Pay pay = new Pay.Builder().chargeAmount(payment.getAmount()).build();
        return new VoiceResponse.Builder().say(say).build();
    }

    public Payment getPaymentFromCallSid(String callSid) {
        return this.paymentRepository.findByCallSid(callSid).orElseThrow(NoSuchElementException::new);
    }

    public List<Payment> getAllPayments() {
        return this.paymentRepository.findAll();
    }

    public String getUrl() {
        System.out.println(this.twilioConfiguration.getUrl());
        return this.twilioConfiguration.getUrl();
    }
}
