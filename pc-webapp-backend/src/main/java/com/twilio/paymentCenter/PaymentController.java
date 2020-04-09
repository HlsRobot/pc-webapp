package com.twilio.paymentCenter;

import com.twilio.twiml.VoiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("api")
public class PaymentController {

    private final SmsService smsService;
    private final PaymentPhoneService paymentPhoneService;

    @Autowired
    public PaymentController(final SmsService smsService, final PaymentPhoneService paymentPhoneService) {
        this.smsService = smsService;
        this.paymentPhoneService = paymentPhoneService;
    }

    @PostMapping("/sms")
    public void sendSms(@Valid @RequestBody final SmsRequest smsRequest) {
        this.smsService.sendSms(smsRequest);
    }

    @PostMapping("/call")
    public ResponseEntity<String> makeACall(@RequestBody final PaymentRequest paymentRequest) throws URISyntaxException {
        final String callSid = this.paymentPhoneService.makeAPaymentCall(paymentRequest);
        return new ResponseEntity<>(callSid, HttpStatus.OK);
    }

    @PostMapping("/voice")
    public void voiceApp(final HttpServletRequest request, final HttpServletResponse response) throws IOException {


        final String callSid = request.getParameter("CallSid");

        final VoiceResponse voiceResponse = this.paymentPhoneService.getCorrectVoiceResponse(callSid);

        response.setContentType("application/xml");
        response.getWriter().print(voiceResponse.toXml());
    }

    @PostMapping("/credit")
    public void getCreditCardNumber(@RequestParam(value = "Digits", required = false) final String digits,
                                    final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        System.out.println(digits);
        final VoiceResponse voiceResponse = this.paymentPhoneService.handleCreditCardNumber(digits, request.getParameter("CallSid"));
        response.setContentType("application/xml");
        response.getWriter().print(voiceResponse.toXml());
    }

    @PostMapping("/owner")
    public void getOwner(@RequestParam(value = "SpeechResult", required = false) final String owner,
                                    final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        System.out.println(owner);
        final String sanitizedOwner = owner != null ? owner.toUpperCase() : null;
        final VoiceResponse voiceResponse = this.paymentPhoneService.handleOwner(sanitizedOwner, request.getParameter("CallSid"));
        response.setContentType("application/xml");
        response.getWriter().print(voiceResponse.toXml());
    }

    @PostMapping("/date")
    public void getExpirationDate(@RequestParam(value = "Digits", required = false) final String date,
                         final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        System.out.println(date);
        final VoiceResponse voiceResponse = this.paymentPhoneService.handleDate(date, request.getParameter("CallSid"));
        response.setContentType("application/xml");
        response.getWriter().print(voiceResponse.toXml());
    }

    @PostMapping("/finish")
    public void getSecurityNumber(@RequestParam(value = "Digits", required = false) final String securityNumber,
                                  final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        System.out.println(securityNumber);
        final VoiceResponse voiceResponse = this.paymentPhoneService.handleSecurityNumber(securityNumber, request.getParameter("CallSid"));
        response.setContentType("application/xml");
        response.getWriter().print(voiceResponse.toXml());
    }

    @GetMapping("/update")
    public Payment getPaymentFromCallSid(@RequestParam(value = "callSid") final String callSid) {
        return this.paymentPhoneService.getPaymentFromCallSid(callSid);
    }

    @GetMapping("/get_payments")
    public List<Payment> getAllPayments() {
        return this.paymentPhoneService.getAllPayments();
    }

    @GetMapping("/get_url")
    public String getUrl() {
        return this.paymentPhoneService.getUrl();
    }

}
