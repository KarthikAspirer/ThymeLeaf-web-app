package com.java.Themeleaf.service;

import com.java.Themeleaf.entity.Department;
import com.java.Themeleaf.entity.Student;
import com.java.Themeleaf.exception.FileUploadException;
import com.java.Themeleaf.exception.StudentNotFoundException;
import com.java.Themeleaf.repository.DepartmentRepository;
import com.java.Themeleaf.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final UserLogService userLogService;

    public Page<Student> getStudentsWithPagination(int pageNo, int pageSize) {
        return studentRepository.findAll(PageRequest.of(pageNo, pageSize));
    }
    @Override
//    @Cacheable(
//            value = "studentByName",
//            key = "#firstName"
//    )
    @PreAuthorize("hasRole('ADMIN')")
    public Student getStudentByFirstName(String firstName) {
        System.out.println("Fetching student from DB...");
        return studentRepository
                .findByFirstNameIgnoreCase(firstName)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with name '" + firstName + "' not found"));
    }

    @Override
    public void saveAll(List<Student> students) {

        studentRepository.saveAll(students);
    }

    @Override
//    @CacheEvict(value = "studentByName", allEntries = true)
    public void saveStudentsFromFile(MultipartFile file) {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length < 3) {
                    throw new FileUploadException("Invalid CSV format");
                }
                Student s = new Student();
                s.setFirstName(data[0].trim());
                s.setLastName(data[1].trim());
                s.setEmail(data[2].trim());
                students.add(s);
            }
            studentRepository.saveAll(students);
        } catch (IOException e) {
            throw new FileUploadException("Failed to read CSV file");
        }
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }


    @Override
//    @CacheEvict(value = "studentByName", allEntries = true)
    @PreAuthorize("hasRole('USER')")
    public Student saveStudent(Student student,Long departmentId) {
        userLogService.log("CREATE",student.getFirstName());
        Department dept =
                departmentRepository.findById(departmentId)
                        .orElseThrow(() -> new RuntimeException("Department not found"));
        student.setDepartment(dept);
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {

        return studentRepository.findById(id).get();
    }

    @Override
//    @CacheEvict(value = "studentByName", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
//    @CacheEvict(value = "studentByName", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudentById(Long id) {
       Student student = studentRepository.findById(id).orElseThrow(() ->
                new StudentNotFoundException(
                        "Student with ID '" + id + "' not found"));

        userLogService.log("DELETE",student.getFirstName());
        studentRepository.deleteById(id);
    }
}
