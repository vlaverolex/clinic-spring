package com.vladveretilnyk.clinic.repository;

import com.vladveretilnyk.clinic.entity.Nurse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NurseRepository extends JpaRepository<Nurse, Long> {

}
