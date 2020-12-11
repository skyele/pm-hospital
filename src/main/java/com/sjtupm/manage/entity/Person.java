package com.sjtupm.manage.entity;

import javax.persistence.*;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="role") /* Admin:0 Doctor:1 Patient:2 */
    private Integer role;

    @Column(name = "name")
    private String name;

    public Integer getId() {
        return id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
