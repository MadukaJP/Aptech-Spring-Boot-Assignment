package com.aptech.springintro.api;

import com.aptech.springintro.model.Student;
import com.aptech.springintro.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentApiController {

    @Autowired
    private StudentRepository studentRepo;

    @Value("${API_SECRET_KEY}")
    private String apiSecretKey;

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(
            @PathVariable int id,
            @RequestHeader("X-API-KEY") String xApiKey) {

        if (xApiKey == null || !xApiKey.equals(apiSecretKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Invalid API key");
        }

        return studentRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}