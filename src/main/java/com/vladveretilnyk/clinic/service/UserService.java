package com.vladveretilnyk.clinic.service;

import com.vladveretilnyk.clinic.entity.*;
import com.vladveretilnyk.clinic.repository.DoctorRepository;
import com.vladveretilnyk.clinic.repository.NurseRepository;
import com.vladveretilnyk.clinic.repository.PatientRepository;
import com.vladveretilnyk.clinic.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final PatientRepository patientRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);


    public UserService(UserRepository userRepository, DoctorRepository doctorRepository, NurseRepository nurseRepository, PatientRepository patientRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.nurseRepository = nurseRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
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

    public boolean assignDoctorForPatient(Long patientId, Long doctorId) {
        Patient patient = (Patient) findUserById(patientId);
        Doctor doctor = (Doctor) findUserById(doctorId);
        patient.setDoctor(doctor);
        save(patient);
        LOGGER.info("Assigned doctor = {} for patient = {}", doctor, patient);
        return true;
    }

    public boolean assignNurseForPatient(Long patientId, Long nurseId) {
        Patient patient = (Patient) findUserById(patientId);
        Nurse nurse = (Nurse) findUserById(nurseId);
        patient.setNurse(nurse);
        save(patient);
        LOGGER.info("Assigned nurse = {} for patient = {}", nurse, patient);
        return true;
    }

    public boolean removeDoctorForPatient(Long id) {
        Patient patient = (Patient) findUserById(id);
        patient.removeDoctor();
        save(patient);
        LOGGER.info("Removed doctor for patient = {}", patient);
        return true;
    }

    public boolean removeNurseForPatient(Long id) {
        Patient patient = (Patient) findUserById(id);
        patient.removeNurse();
        save(patient);
        LOGGER.info("Removed nurse for patient = {}", patient);
        return true;
    }

    public boolean createNoteForPatient(Long id, Note note) {
        note.setCreationDate(LocalDate.now());
        Patient patient = (Patient) findUserById(id);
        patient.addMedicalNote(note);
        save(patient);
        LOGGER.info("Created note = {} for patient = {}", note, patient);
        return true;
    }
}
