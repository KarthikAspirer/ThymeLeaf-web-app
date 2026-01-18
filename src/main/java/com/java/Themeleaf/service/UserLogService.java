package com.java.Themeleaf.service;

import com.java.Themeleaf.entity.Student;
import com.java.Themeleaf.entity.UserLog;
import com.java.Themeleaf.exception.StudentNotFoundException;
import com.java.Themeleaf.repository.StudentRepository;
import com.java.Themeleaf.repository.UserLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class UserLogService {
    private final UserLogRepository userLogRepository;
    private final StudentRepository studentRepository;

    public UserLogService(UserLogRepository userLogRepository,StudentRepository studentRepository) {

        this.userLogRepository = userLogRepository;
        this.studentRepository = studentRepository;
    }

    public void log(String operation,String firstName){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth !=null ? auth.getName() : "UNKNOWN";

      Student student = studentRepository
                .findByFirstNameIgnoreCase(firstName)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with name '" + firstName + "' not found"));

        UserLog log = new UserLog();
        log.setUserName(username);
        log.setStudentName(student.getFirstName());
        log.setOperation(operation);
        log.setActionTimestamp(LocalDateTime.now());
        userLogRepository.save(log);
    }
}
