package com.example.eventmanager.models;

import java.io.Serializable;

public class User implements Serializable {
    public String key;

    public User() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
