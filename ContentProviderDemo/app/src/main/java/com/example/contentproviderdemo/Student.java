package com.example.contentproviderdemo;

/**
 * Created by sz132 on 2017/6/12.
 */

public class Student {
    private Long id;
    private String name;
    private String phone;

    public Student(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
