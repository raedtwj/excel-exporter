package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    public String first_name;
    public String last_name;
    public String email;
    public String organization;
    public String subscription;
    public Customer() {
        
    }
}