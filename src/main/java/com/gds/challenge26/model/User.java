package com.gds.challenge26.model;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String name;

    public User() {}

    public User(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
