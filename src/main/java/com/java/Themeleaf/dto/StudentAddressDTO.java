package com.java.Themeleaf.dto;

import lombok.Data;

@Data
public class StudentAddressDTO {
    private String firstName;
    private String lastName;
    private String email;

    private String street;
    private String city;
    private String state;
    private String pincode;
}
