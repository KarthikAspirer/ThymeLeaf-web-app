package com.java.Themeleaf.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "address")
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String state;
    private String pincode;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

}
