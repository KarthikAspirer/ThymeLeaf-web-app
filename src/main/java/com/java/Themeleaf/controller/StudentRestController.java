package com.java.Themeleaf.controller;

import com.java.Themeleaf.dto.StudentAddressDTO;
import com.java.Themeleaf.entity.Address;
import com.java.Themeleaf.entity.Student;
import com.java.Themeleaf.exception.FileUploadException;
import com.java.Themeleaf.helperClass.StudentListWrapper;
import com.java.Themeleaf.repository.StudentRepository;
import com.java.Themeleaf.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
public class StudentRestController {
    private final StudentRepository studentRepository;

    @GetMapping("/address")
    @PreAuthorize("hasRole('ADMIN')")
    public StudentAddressDTO getStudentAddress(
            @RequestParam String firstName) {
        System.out.println("firstName "+firstName);
        Student student = studentRepository
                .findByFirstNameIgnoreCase(firstName)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        System.out.println("student "+student);
        Address address = student.getAddress();

        StudentAddressDTO dto = new StudentAddressDTO();
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPincode(address.getPincode());

        return dto;
    }


}
