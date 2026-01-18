package com.java.Themeleaf.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String StudentName;
    private String operation;

    @Column(nullable = false)
    private LocalDateTime actionTimestamp;
}
