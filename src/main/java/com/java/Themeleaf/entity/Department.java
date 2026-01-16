package com.java.Themeleaf.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="department")
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
}
