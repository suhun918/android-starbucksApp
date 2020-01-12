package com.cos.mystarbucks.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String username;
    private String email;

    private String provider;
    private String providerId;

    private String level;
    private String createDate;

    private String cookie;

    private static User u = null;

    private User(){
    }

    public static User getInstance() {
        if (u == null)
            u = new User();
        return u;
    }
}
