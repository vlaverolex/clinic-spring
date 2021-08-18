package com.vladveretilnyk.clinic.repository;

import com.vladveretilnyk.clinic.entity.Category;
import com.vladveretilnyk.clinic.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
