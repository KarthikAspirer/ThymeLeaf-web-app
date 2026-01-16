package com.java.Themeleaf.service;

import com.java.Themeleaf.entity.Department;
import com.java.Themeleaf.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student saveStudent(Student student,Long departmentId);
    Student getStudentById(Long id);
    Student updateStudent(Student student);
    void deleteStudentById(Long id);
    Page<Student> getStudentsWithPagination(int pageNo, int pageSize);
    Student getStudentByFirstName(String firstName);

    void saveAll(List<Student> students);

    void saveStudentsFromFile(MultipartFile file);
    List<Department> getAllDepartments();
}
