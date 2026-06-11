package com.aptech.springintro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.springintro.model.Student;

import java.util.List;

// ════════════════════════════════════════════════════════════
// THE REPOSITORY


@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByNameContainingIgnoreCase(String name);
    
    // Example: "Find students with grade A"
    List<Student> findByGrade(String grade);

    List<Student> findByActiveTrue();
}
