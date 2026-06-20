package com.aptech.courseservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.aptech.courseservice.dto.StudentDTO;

// 1. The DTO (Data Transfer Object)
// This must match the JSON structure returned by the Student Service.
// A Java Record is perfect for DTOs because it's immutable and concise.



// 2. The Feign Client Interface
// name = A logical name for the client (used if Eureka is enabled)
// url  = The hardcoded address of the target service (used if Eureka is NOT enabled)
@FeignClient(name = "student-service", url = "${student.service.url}")
public interface StudentClient {

    // 3. The Mapping
    // This looks EXACTLY like a standard @RestController mapping.
    // But instead of receiving a request, it SENDS a request to this path.
    @GetMapping("/api/students/{id}")
    StudentDTO getStudentById(@PathVariable("id") int id);
    
    // Spring Boot writes the implementation of this interface dynamically at runtime!
}