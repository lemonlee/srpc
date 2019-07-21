package com.lemon.model;

import java.io.Serializable;

/**
 * Created by lihuihua on 2019/1/25.
 */
public class Message implements Serializable{
    private static final long serialVersionUID = -7432143972263049266L;
    private int id;
    private String content;

    public Message(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
