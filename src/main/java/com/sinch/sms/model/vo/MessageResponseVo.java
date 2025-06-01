package com.sinch.sms.model.vo;

import com.sinch.sms.model.constant.MessageStatus;

public class MessageResponseVo {
    private String id;
    private String carrier;
    private MessageStatus status;

    public MessageResponseVo() {

    }

    public MessageResponseVo(String id, MessageStatus status, String carrier) {
        this.id = id;
        this.carrier = carrier;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}
