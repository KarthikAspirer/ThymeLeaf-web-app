package com.java.Themeleaf.controller;


import com.java.Themeleaf.dto.StudentAddressDTO;
import com.java.Themeleaf.entity.Address;
import com.java.Themeleaf.entity.Student;
import com.java.Themeleaf.entity.User;
import com.java.Themeleaf.exception.FileUploadException;
import com.java.Themeleaf.helperClass.StudentListWrapper;
import com.java.Themeleaf.repository.StudentRepository;
import com.java.Themeleaf.repository.UserRepository;
import com.java.Themeleaf.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class StudentController {

    private final RestTemplate restTemplate;
    private final StudentService studentService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/students")
    //http://localhost:8080/students
    public String listStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Page<Student> studentPage = studentService.getStudentsWithPagination(page, size);
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        return "students";
    }

    // 🔹 Open bulk add page
    @GetMapping("/students/bulk")
    //http://localhost:8080/students/bulk
    public String showBulkAddPage(Model model) {
        model.addAttribute("students", new ArrayList<Student>());
        return "add-multiple-students";
    }
    // 🔹 upload file
    @PostMapping("/students/upload")
    public String uploadStudents(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileUploadException("Uploaded file is empty");
        }
        studentService.saveStudentsFromFile(file);
        return "redirect:/students";
    }

    // 🔹 Save multiple students
    @PostMapping("/students/bulk/save")
    public String saveBulkStudents(
            @ModelAttribute("students") StudentListWrapper wrapper) {

        studentService.saveAll(wrapper.getStudents());
        return "redirect:/students";
    }

    @GetMapping("/students/view-by-name")
    //http://localhost:8080/students/view-by-name?firstName="karthik"
    public String viewStudentByName(
            @RequestParam String firstName,
            Model model) {
        Student student = studentService.getStudentByFirstName(firstName);
        model.addAttribute("student", student);
        System.out.println("Department details "+student.getDepartment().getName());
        return "view-student";
    }
    @GetMapping("/students/new")
    //http://localhost:8080/students/new
    public String createStudentForm(Model model){
        model.addAttribute("student", new Student());
        model.addAttribute("departments",
                studentService.getAllDepartments());
        return "create_student";
    }
    @PostMapping("/students")
    //http://localhost:8080/students?departmentId=1
    public String saveStudent( @Valid @ModelAttribute("student") Student student,@RequestParam("departmentId") Long departmentId,BindingResult result){
        if (result.hasErrors()) {
            return "create_student"; // same page
        }
        studentService.saveStudent(student,departmentId);
        return "redirect:/students";
    }
    @GetMapping("/students/edit/{id}")
    //http://localhost:8080/students
    public String editStudentForm(@PathVariable Long id, Model model){
        model.addAttribute("student",studentService.getStudentById(id));
        return "edit_student";
    }
    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id,@ModelAttribute("student") Student student,Model model){
       Student existingStudent = studentService.getStudentById(id);
        existingStudent.setId(id);
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        studentService.updateStudent(existingStudent);
        return "redirect:/students";

    }
    @GetMapping("/students/{id}")
        public String deleteStudent(@PathVariable Long id){
            studentService.deleteStudentById(id);
            return "redirect:/students";
        }

    @GetMapping("/students/download")
    public void downloadStudents(HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition",
                "attachment; filename=students.csv");

        List<Student> students = studentService.getAllStudents();

        PrintWriter writer = response.getWriter();

        // CSV Header (optional but recommended)
        writer.println("FirstName,LastName,Email");

        for (Student s : students) {
            writer.println(
                    s.getFirstName() + "," +
                            s.getLastName() + "," +
                            s.getEmail()
            );
        }

        writer.flush();
        writer.close();
    }

   // @PreAuthorize("hasRole('ADMIN','USER')")
    @GetMapping("/students/address-search")
    public String showSearchPage(
            @RequestParam(required = false) String firstName,
            Model model) {

        if (firstName != null) {
            String url = "http://localhost:8080/api/students/address?firstName=" + firstName;
            String sessionId = request.getSession().getId();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", "JSESSIONID=" + sessionId);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<StudentAddressDTO> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                          // null,
                            entity,
                            StudentAddressDTO.class
                    );


            model.addAttribute("address", response.getBody());
        }

        return "student-address-search";
    }


}
