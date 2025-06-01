package com.sinch.sms.model.entity;

import com.sinch.sms.model.constant.MessageStatus;

import java.time.LocalDateTime;

public class Message {
    private String id;
    private String destinationNumber;
    private String content;
    private String carrier;
    private MessageStatus status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public Message(String id, String destinationNumber, String content, String carrier,
                   MessageStatus status, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.destinationNumber = destinationNumber;
        this.content = content;
        this.carrier = carrier;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
