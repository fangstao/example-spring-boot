package com.example.spring.boot;

/**
 * Created by pc on 2016/6/28.
 */
public class User {
    private String name;
    private Integer id;

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public final static User create(Integer id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }
}
