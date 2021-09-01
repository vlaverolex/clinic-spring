package com.vladveretilnyk.clinic.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Setter
@Getter
@ToString
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;

    private String diagnosis;

    @Column(length = 3000)
    private String procedures;

    private String surgery;

    public void setSurgery(String surgery) {
        if (!surgery.isEmpty()) {
            this.surgery = surgery;
        }
    }

    private boolean isProceduresDone;

    private boolean isSurgeryDone;

    private Long doctorIdWhoCreatedNote;

    private Long personIdWhoMadeProcedures;

    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return Objects.equals(id, note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

