package com.example.visitus.data;

public class event_bus {
    private String msg;

    public event_bus(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
