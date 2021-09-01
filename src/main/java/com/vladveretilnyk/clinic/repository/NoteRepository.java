package com.vladveretilnyk.clinic.repository;

import com.vladveretilnyk.clinic.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findNotesByPatientId(Long id, Pageable pageable);
}
