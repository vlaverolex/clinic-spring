package com.vladveretilnyk.clinic.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "nurse")
@Getter
@Setter
public class Nurse extends User {

    public Nurse() {
        addRole(new Role(3L));
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private int patientVolume;

    @OneToMany(mappedBy = "nurse")
    private Set<Patient> patients = new HashSet<>();

    public void addPatient(Patient patient) {
        patients.add(patient);
        patientVolume++;
    }

    public void removePatient(Patient patient) {
        patients.remove(patient);
        patientVolume--;
    }
}
