package com.vladveretilnyk.clinic.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patient")
@Getter
@Setter
public class Patient extends User {

    public Patient() {
        addRole(new Role(4L));
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Getter
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Note> notes = new ArrayList<>();

    public void addMedicalNote(Note note) {
        notes.add(note);
        note.setPatient(this);
    }

    public void removeNote(Note note) {
        notes.remove(note);
        note.setPatient(null);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        this.doctor.addPatient(this);
    }

    public void removeDoctor() {
        if (doctor != null) {
            doctor.removePatient(this);
            doctor = null;
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Nurse nurse;

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
        this.nurse.addPatient(this);
    }

    public void removeNurse() {
        if (nurse != null) {
            nurse.removePatient(this);
            nurse = null;
        }
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", birthday=" + birthday +
                ", doctor=" + doctor +
                ", nurse=" + nurse +
                '}';
    }
}
