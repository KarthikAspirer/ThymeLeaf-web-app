package com.java.Themeleaf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name="students")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name",nullable = false)
    @NotBlank(message = "First name is required")
    private String firstName;
    @Column(name = "last_name")
    @NotBlank(message = "Last name is required")
    private String lastName;
    @Column(name = "email")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    // 🔥 Hibernate Mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private Address address;
}
