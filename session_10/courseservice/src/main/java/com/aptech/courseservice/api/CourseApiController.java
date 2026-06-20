package com.aptech.courseservice.api;

import com.aptech.courseservice.model.Course;
import com.aptech.courseservice.model.Enrollment;
import com.aptech.courseservice.repository.CourseRepository;
import com.aptech.courseservice.repository.EnrollmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/course")
public class CourseApiController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @PostMapping
    public Course addCourse(@RequestBody Course course) {
        return courseRepository.save(course);
    }

    @PostMapping("/enroll")
    public ResponseEntity<?> enrollStudent(
            @RequestParam String studentName,
            @RequestParam int courseId) {

        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Course not found with ID " + courseId));
        }

        Enrollment enrollment = new Enrollment(studentName, courseId, course.getTitle());
        enrollmentRepository.save(enrollment);

        return ResponseEntity.ok(Map.of(
                "message", studentName + " successfully enrolled in " + course.getTitle(),
                "enrollmentId", enrollment.getId()
        ));
    }

    @DeleteMapping("/enroll")
    public ResponseEntity<?> unenrollStudent(
            @RequestParam String studentName,
            @RequestParam int courseId) {

        Enrollment enrollment = enrollmentRepository
                .findByStudentNameAndCourseId(studentName, courseId).orElse(null);

        if (enrollment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Enrollment not found for " + studentName));
        }

        enrollmentRepository.delete(enrollment);

        return ResponseEntity.ok(Map.of(
                "message", studentName + " unenrolled successfully"
        ));
    }

    @GetMapping("/enrolled")
    public ResponseEntity<?> getEnrolledCourses(@RequestParam String studentName) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentName(studentName);
        List<Integer> courseIds = enrollments.stream()
                .map(Enrollment::getCourseId)
                .toList();
        return ResponseEntity.ok(courseIds);
    }

    @GetMapping("/{courseId}/students")
    public ResponseEntity<?> getCourseStudents(@PathVariable int courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        List<String> studentNames = enrollments.stream()
                .map(Enrollment::getStudentName)
                .toList();
        return ResponseEntity.ok(Map.of(
                "courseId", courseId,
                "students", studentNames,
                "count", studentNames.size()
        ));
    }
}
