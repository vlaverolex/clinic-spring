package com.vladveretilnyk.clinic.repository;

import com.vladveretilnyk.clinic.entity.Doctor;
import com.vladveretilnyk.clinic.entity.Nurse;
import com.vladveretilnyk.clinic.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findPatientsByDoctor(Doctor doctor, Pageable pageable);

    Page<Patient> findPatientsByNurse(Nurse nuser, Pageable pageable);
}
