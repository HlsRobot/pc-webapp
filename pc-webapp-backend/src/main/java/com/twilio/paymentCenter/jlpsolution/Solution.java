package com.twilio.paymentCenter.jlpsolution;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "solution")
public class Solution {

    @Id
    @GeneratedValue
    private Long id;

    private String callSid;

    private String phone;

    private String trade;

    private String zipCode;

    private String job;

    private String jobDetail;

    private String cost;

    private String date;

    private String success;

    public Solution(final String callSid, final String phone, final String trade, final String zipCode,
                    final String job, final String jobDetail, final String cost, final String date, final String success) {
        this.callSid = callSid;
        this.phone = phone;
        this.trade = trade;
        this.zipCode = zipCode;
        this.job = job;
        this.jobDetail = jobDetail;
        this.cost = cost;
        this.date = date;
        this.success = success;
    }

    public Solution(final String callSid, final String phone) {
        this.callSid = callSid;
        this.phone = phone;
    }

    public Solution() {
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

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getTrade() {
        return this.trade;
    }

    public void setTrade(final String trade) {
        this.trade = trade;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }

    public String getJob() {
        return this.job;
    }

    public void setJob(final String job) {
        this.job = job;
    }

    public String getJobDetail() {
        return this.jobDetail;
    }

    public void setJobDetail(final String jobDetail) {
        this.jobDetail = jobDetail;
    }

    public String getCost() {
        return this.cost;
    }

    public void setCost(final String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getSuccess() {
        return this.success;
    }

    public void setSuccess(final String success) {
        this.success = success;
    }
}
