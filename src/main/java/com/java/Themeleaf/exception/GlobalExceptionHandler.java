package com.java.Themeleaf.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(StudentNotFoundException.class)
    public String handleStudentNotFound(
            StudentNotFoundException ex,
            Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error-student";
    }
//
//    @ExceptionHandler(Exception.class)
//    public String handleException(Model model) {
//        model.addAttribute("message",
//                "Invalid input submitted. Please try again.");
//        return "validation-error";
//    }
    @ExceptionHandler(FileUploadException.class)
    public String handleFileUploadException(
            FileUploadException ex,
            Model model) {
        model.addAttribute("errorTitle", "File Upload Error");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error-page";
    }
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Model model) {
        model.addAttribute("errorTitle", "Unexpected Error");
        model.addAttribute("errorMessage",
                "Something went wrong. Please try again.");
        return "error-page";
    }
}
