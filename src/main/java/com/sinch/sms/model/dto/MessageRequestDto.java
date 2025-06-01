package com.sinch.sms.model.dto;

public class MessageRequestDto {
    private String destinationNumber;
    private String content;
    private String format;

    public MessageRequestDto(String destinationNumber, String content, String format) {
        this.destinationNumber = destinationNumber;
        this.content = content;
        this.format = format;
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
