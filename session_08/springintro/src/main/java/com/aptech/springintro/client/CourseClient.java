package com.aptech.springintro.client;

import com.aptech.springintro.dto.CourseDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(
    name = "course-service",
    url = "${course.service.url}"
)
public interface CourseClient {

    @GetMapping("/course")
    List<CourseDTO> getAllCourses();

    @PostMapping("/api/course/enroll")
    Map<String, Object> enrollStudent(
            @RequestParam("studentName") String studentName,
            @RequestParam("courseId") int courseId);

    @DeleteMapping("/api/course/enroll")
    Map<String, Object> unenrollStudent(
            @RequestParam("studentName") String studentName,
            @RequestParam("courseId") int courseId);

    @GetMapping("/api/course/enrolled")
    List<Integer> getEnrolledCourses(@RequestParam("studentName") String studentName);

    @GetMapping("/api/course/{courseId}/students")
    Map<String, Object> getCourseStudents(@PathVariable("courseId") int courseId);
}
