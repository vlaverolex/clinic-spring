package com.vladveretilnyk.clinic.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctor")
@Getter
@Setter
public class Doctor extends User {

    public Doctor() {
        addRole(new Role(2L));
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private int patientVolume;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    public void setCategory(Category category) {
        this.category = category;
        this.category.addDoctor(this);
    }

    public void removeCategory() {
        if (category != null) {
            category.removeDoctor(this);
            category = null;
        }
    }

    @OneToMany(mappedBy = "doctor")
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
