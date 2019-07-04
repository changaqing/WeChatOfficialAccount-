package com.zhishi.wechat.entity;

public class JsapiTicketEntity extends ErrorEntity {
    private String ticket;//临时票据
    private Integer expires_in;//时效

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }
}
