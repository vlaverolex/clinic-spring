package com.vladveretilnyk.clinic.service;

import com.vladveretilnyk.clinic.entity.*;
import com.vladveretilnyk.clinic.exception.NoteNotFoundException;
import com.vladveretilnyk.clinic.exception.UserNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class UserServiceTest {

    @Autowired
    private UserService userService;


    @Test(expected = DataIntegrityViolationException.class)
    public void tryingToSaveAnExistingUserShouldThrowAnException() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.addRole(new Role(1L));
        userService.save(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void findByIdShouldThrowAnExceptionWhenUserNotFound() throws UserNotFoundException {
        userService.findById(-2L);
    }

    @Test
    public void allDoctorsShouldHaveDoctorRole() {
        List<Doctor> doctors = userService.findAllDoctors();
        Assert.assertEquals(doctors.size(),
                doctors.stream().filter(user -> user.getRoles().contains(new Role(2L))).count());
    }

    @Test
    public void allNursesShouldHaveNurseRole() {
        List<Nurse> nurses = userService.findAllNurses();
        Assert.assertEquals(nurses.size(),
                nurses.stream().filter(user -> user.getRoles().contains(new Role(3L))).count());
    }

    @Test
    public void allPatientsShouldHavePatientRole() {
        List<Patient> patients = userService.findAllPatients();
        Assert.assertEquals(patients.size(),
                patients.stream().filter(user -> user.getRoles().contains(new Role(4L))).count());
    }

    @Rollback
    @Test
    public void patientsShouldHaveDoctor() throws UserNotFoundException {
        Doctor doctor = new Doctor();
        doctor.setUsername("test_doctor");
        doctor.setPassword("test_doctor");

        userService.save(doctor);

        for (int i = 0; i < 100; i++) {
            Patient patient = new Patient();
            patient.setUsername("patient_test" + i);
            patient.setPassword("patient_test");
            patient.setDoctor(doctor);
            userService.save(patient);
        }

        List<Patient> patients = userService.findPatientsByDoctorUsername(doctor.getUsername());
        Assert.assertEquals(patients.size(), patients.stream().filter(patient -> patient.getDoctor().getUsername().equals(doctor.getUsername())).count());

        for(Patient patient:patients) userService.delete(patient);
        userService.delete(doctor);
    }

    @Test
    public void patientsShouldHaveNurse() throws UserNotFoundException {
        Nurse nurse = new Nurse();
        nurse.setUsername("test_nurse");
        nurse.setPassword("test_nurse");

        userService.save(nurse);

        for (int i = 0; i < 100; i++) {
            Patient patient = new Patient();
            patient.setUsername("patient_test" + i);
            patient.setPassword("patient_test");
            patient.setNurse(nurse);
            userService.save(patient);
        }

        List<Patient> patients = userService.findPatientsByNurseUsername(nurse.getUsername());
        Assert.assertEquals(patients.size(), patients.stream().filter(patient -> patient.getNurse().getUsername().equals(nurse.getUsername())).count());

        for(Patient patient:patients) userService.delete(patient);
        userService.delete(nurse);
    }

    @Test
    public void patientShouldHaveSpecificDoctorAfterAssigning() throws UserNotFoundException {
        Patient patient = new Patient();
        Doctor doctor = new Doctor();

        patient.setUsername("test_patient");
        patient.setPassword("test_patient");

        doctor.setUsername("test_doctor");
        doctor.setPassword("test_doctor");

        userService.save(patient);
        userService.save(doctor);

        Assert.assertEquals(doctor.getId(), userService.assignDoctorForPatient
                (patient.getId(), doctor.getId()).getDoctor().getId());

        userService.delete(patient);
        userService.delete(doctor);
    }

    @Test
    public void patientShouldHaveSpecificNurseAfterAssigning() throws UserNotFoundException {
        Patient patient = new Patient();
        Nurse nurse = new Nurse();

        patient.setUsername("test_patient");
        patient.setPassword("test_patient");

        nurse.setUsername("test_doctor");
        nurse.setPassword("test_doctor");

        userService.save(patient);
        userService.save(nurse);

        Assert.assertEquals(nurse.getId(), userService.assignNurseForPatient
                (patient.getId(), nurse.getId()).getNurse().getId());

        userService.delete(patient);
        userService.delete(nurse);
    }

    @Test
    public void patientShouldNotHaveNurseAfterRemoving() throws UserNotFoundException {
        Patient patient = new Patient();
        Nurse nurse = new Nurse();

        patient.setUsername("test_patient");
        patient.setPassword("test_patient");

        nurse.setUsername("test_nurse");
        nurse.setPassword("test_nurse");

        patient.setNurse(nurse);

        userService.save(patient);
        userService.save(nurse);

        assertNull(userService.removeNurseForPatient(patient.getId()).getNurse());

        userService.delete(patient);
        userService.delete(nurse);
    }

    @Test
    public void patientShouldNotHaveDoctorAfterRemoving() throws UserNotFoundException {
        Patient patient = new Patient();
        Doctor doctor = new Doctor();

        patient.setUsername("test_patient");
        patient.setPassword("test_patient");

        doctor.setUsername("test_doctor");
        doctor.setPassword("test_doctor");

        patient.setDoctor(doctor);

        userService.save(patient);
        userService.save(doctor);

        assertNull(userService.removeDoctorForPatient(patient.getId()).getDoctor());

        userService.delete(patient);
        userService.delete(doctor);
    }

    @Test
    public void patientShouldHaveNoteAfterNoteCreation() throws UserNotFoundException {
        Patient patient = new Patient();
        Doctor doctor = new Doctor();
        Note note = new Note();

        note.setDiagnosis("diagnosis");
        note.setProcedures("procedures");
        note.setSurgery("surgery");

        patient.setUsername("test_patient");
        patient.setPassword("test_patient");

        doctor.setUsername("test_doctor");
        doctor.setPassword("test_doctor");

        userService.save(patient);
        userService.save(doctor);

        Assert.assertTrue(userService.createNoteForPatient(patient.getId(), note, doctor.getUsername()).getNotes().contains(note));
        userService.delete(patient);
    }

    @Test
    public void proceduresShouldBeDoneAfterMaking() throws UserNotFoundException, NoteNotFoundException {
        Patient patient = new Patient();
        Doctor doctor = new Doctor();
        Note note = new Note();

        note.setDiagnosis("diagnosis");
        note.setProcedures("procedures");
        note.setSurgery("surgery");

        patient.setUsername("test_patient");
        patient.setPassword("test_patient");

        doctor.setUsername("test_doctor");
        doctor.setPassword("test_doctor");

        userService.save(patient);
        userService.save(doctor);

        userService.createNoteForPatient(patient.getId(), note, doctor.getUsername());

        Assert.assertTrue(userService.makeProcedureForPatient(patient.getId(), note.getId(), doctor.getUsername()).isProceduresDone());
        userService.delete(patient);
    }

    @Test
    public void surgeryShouldBeDoneAfterMaking() throws UserNotFoundException, NoteNotFoundException {
        Patient patient = new Patient();
        Doctor doctor = new Doctor();
        Note note = new Note();

        note.setDiagnosis("diagnosis");
        note.setProcedures("procedures");
        note.setSurgery("surgery");

        patient.setUsername("test_patient");
        patient.setPassword("test_patient");

        doctor.setUsername("test_doctor");
        doctor.setPassword("test_doctor");

        userService.save(patient);
        userService.save(doctor);

        userService.createNoteForPatient(patient.getId(), note, doctor.getUsername());

        Assert.assertTrue(userService.makeSurgeryForPatient(patient.getId(), note.getId()).isSurgeryDone());
        userService.delete(patient);
    }
}