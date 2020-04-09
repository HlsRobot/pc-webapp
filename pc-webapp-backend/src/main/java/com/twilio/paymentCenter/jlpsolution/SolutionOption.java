package com.twilio.paymentCenter.jlpsolution;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "solutionOption")
public class SolutionOption {

    @Id
    @GeneratedValue
    private Long id;

    private String phoneId;

    private String type;

    private String voice;

    private String previous;

    private String next;

    public SolutionOption(final String phoneId, final String type, final String voice, final String previous, final String next) {
        this.phoneId = phoneId;
        this.type = type;
        this.voice = voice;
        this.previous = previous;
        this.next = next;
    }

    public SolutionOption() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getPhoneId() {
        return this.phoneId;
    }

    public void setPhoneId(final String phoneId) {
        this.phoneId = phoneId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getVoice() {
        return this.voice;
    }

    public void setVoice(final String voice) {
        this.voice = voice;
    }

    public String getPrevious() {
        return this.previous;
    }

    public void setPrevious(final String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return this.next;
    }

    public void setNext(final String next) {
        this.next = next;
    }
}
