package com.vladveretilnyk.clinic.service;

import com.vladveretilnyk.clinic.entity.Note;
import com.vladveretilnyk.clinic.exception.NoteNotFoundException;
import com.vladveretilnyk.clinic.exception.UserNotFoundException;
import com.vladveretilnyk.clinic.repository.NoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final NoteRepository noteRepository;


    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Page<Note> findNotesByPatientId(Long id, Pageable pageable) {
        return noteRepository.findNotesByPatientId(id, pageable);
    }

    public Note findById(Long id) throws NoteNotFoundException {
        if (noteRepository.findById(id).isPresent()) {
            return noteRepository.findById(id).get();
        }

        throw new NoteNotFoundException("Note with id " + id + " not found!");
    }

    public void save(Note note) {
        noteRepository.save(note);
    }
}
