package com.vladveretilnyk.clinic.service;

import com.vladveretilnyk.clinic.entity.*;
import com.vladveretilnyk.clinic.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final PatientRepository patientRepository;
    private final NoteRepository noteRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);


    public UserService(UserRepository userRepository, DoctorRepository doctorRepository,
                       NurseRepository nurseRepository, PatientRepository patientRepository,
                       BCryptPasswordEncoder passwordEncoder, NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.nurseRepository = nurseRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.noteRepository = noteRepository;
    }

    public Page<Doctor> findAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    public Page<Nurse> findAllNurses(Pageable pageable) {
        return nurseRepository.findAll(pageable);
    }

    public Page<Patient> findAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    public void update(User user) {
        saveAndUpdate(user);
        LOGGER.info("User updated = {}", user);
    }

    public Page<Patient> findPatientsByDoctorUsername(String username, Pageable pageable) {
        return patientRepository.findPatientsByDoctor((Doctor) findUserByUsername(username), pageable);
    }

    public Page<Patient> findPatientsByNurseUsername(String username, Pageable pageable) {
        return patientRepository.findPatientsByNurse((Nurse) findUserByUsername(username), pageable);
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        saveAndUpdate(user);
        LOGGER.info("User saved = {}", user);
    }

    private void saveAndUpdate(User user) {
        if (user.getClass() == Doctor.class) {
            doctorRepository.save((Doctor) user);
        } else if (user.getClass() == Nurse.class) {
            nurseRepository.save((Nurse) user);
        } else if (user.getClass() == Patient.class) {
            patientRepository.save((Patient) user);
        } else {
            userRepository.save(user);
        }
    }

    @Transactional
    public boolean assignDoctorForPatient(Long patientId, Long doctorId) {
        Patient patient = (Patient) findUserById(patientId);
        Doctor doctor = (Doctor) findUserById(doctorId);
        patient.setDoctor(doctor);
        LOGGER.info("Assigning doctor = {} for patient = {}", doctor, patient);
        update(patient);
        return true;
    }

    @Transactional
    public boolean assignNurseForPatient(Long patientId, Long nurseId) {
        Patient patient = (Patient) findUserById(patientId);
        Nurse nurse = (Nurse) findUserById(nurseId);
        patient.setNurse(nurse);
        LOGGER.info("Assigning nurse = {} for patient = {}", nurse, patient);
        update(patient);
        return true;
    }

    @Transactional
    public boolean removeDoctorForPatient(Long id) {
        Patient patient = (Patient) findUserById(id);
        patient.removeDoctor();
        LOGGER.info("Removing doctor for patient = {}", patient);
        update(patient);
        return true;
    }

    @Transactional
    public boolean removeNurseForPatient(Long id) {
        Patient patient = (Patient) findUserById(id);
        patient.removeNurse();
        LOGGER.info("Removing nurse for patient = {}", patient);
        update(patient);
        return true;
    }

    @Transactional
    public boolean createNoteForPatient(Long id, Note note, String doctorUsername) {
        note.setCreationDate(LocalDate.now());
        note.setDoctorIdWhoCreatedNote(findUserByUsername(doctorUsername).getId());
        Patient patient = (Patient) findUserById(id);
        patient.addMedicalNote(note);
        save(patient);
        LOGGER.info("Created note = {} for patient = {}", note, patient);
        return true;
    }

    @Transactional
    public boolean makeProcedureForPatient(Long patientId, Long noteId, String personWhoMakeProcedures) {
        Note note = noteRepository.findNoteById(noteId);
        note.setPersonIdWhoMadeProcedures(findUserByUsername(personWhoMakeProcedures).getId());
        note.setProceduresDone(true);
        update(findUserById(patientId));
        return true;
    }

    @Transactional
    public boolean makeSurgeryForPatient(Long patientId, Long noteId) {
        noteRepository.findNoteById(noteId).setSurgeryDone(true);
        update(findUserById(patientId));
        return true;
    }
}
