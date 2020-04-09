package com.twilio.paymentCenter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "payment")
public class Payment {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    private String callSid;

    private String calledNumber;

    private String amount;

    private String method;

    private String creditCardNumber;

    private String owner;

    private String expirationDate;

    private String securityNumber;

    private Boolean success;

    public Payment(final String callSid, final String calledNumber, final String amount, final String method,
                   final String creditCardNumber, final String owner, final String expirationDate,
                   final String securityNumber, final Boolean success) {
        this.callSid = callSid;
        this.calledNumber = calledNumber;
        this.amount = amount;
        this.method = method;
        this.creditCardNumber = creditCardNumber;
        this.owner = owner;
        this.expirationDate = expirationDate;
        this.securityNumber = securityNumber;
        this.success = success;
    }

    public Payment(final String callSid, final String calledNumber, final String amount, final String method) {
        this.callSid = callSid;
        this.calledNumber = calledNumber;
        this.amount = amount;
        this.method = method;
    }

    public Payment() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCallSid() {
        return this.callSid;
    }

    public void setCallSid(final String callSid) {
        this.callSid = callSid;
    }

    public String getCalledNumber() {
        return this.calledNumber;
    }

    public void setCalledNumber(final String calledNumber) {
        this.calledNumber = calledNumber;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public String getCreditCardNumber() {
        return this.creditCardNumber;
    }

    public void setCreditCardNumber(final String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public String getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(final String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getSecurityNumber() {
        return this.securityNumber;
    }

    public void setSecurityNumber(final String securityNumber) {
        this.securityNumber = securityNumber;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(final Boolean success) {
        this.success = success;
    }
}
