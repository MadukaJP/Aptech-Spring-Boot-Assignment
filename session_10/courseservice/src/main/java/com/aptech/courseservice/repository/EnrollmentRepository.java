package com.aptech.courseservice.repository;

import com.aptech.courseservice.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    List<Enrollment> findByStudentName(String studentName);

    List<Enrollment> findByCourseId(int courseId);

    Optional<Enrollment> findByStudentNameAndCourseId(String studentName, int courseId);
}
