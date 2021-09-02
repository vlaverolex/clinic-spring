package com.vladveretilnyk.clinic.service;

import com.vladveretilnyk.clinic.entity.*;
import com.vladveretilnyk.clinic.exception.NoteNotFoundException;
import com.vladveretilnyk.clinic.exception.UserNotFoundException;
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

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final PatientRepository patientRepository;
    private final NoteService noteService;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);


    public UserService(UserRepository userRepository, DoctorRepository doctorRepository,
                       NurseRepository nurseRepository, PatientRepository patientRepository,
                       BCryptPasswordEncoder passwordEncoder, NoteService noteService) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.nurseRepository = nurseRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.noteService = noteService;
    }

    public Page<Doctor> findAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    public Page<Nurse> findAllNurses(Pageable pageable) {
        return nurseRepository.findAll(pageable);
    }

    public List<Nurse> findAllNurses() {
        return nurseRepository.findAll();
    }

    public Page<Patient> findAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    public User findUserByUsername(String username)  {
        return userRepository.findByUsername(username);
    }

    public User findById(Long id) throws UserNotFoundException {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        }

        throw new UserNotFoundException("User with id " + id + " not found!");
    }

    public void update(User user) {
        saveAndUpdate(user);
        LOGGER.info("User updated = {}", user);
    }

    public void delete(User user) throws UserNotFoundException {
        removeDoctorForPatient(user.getId());
        userRepository.delete(user);
    }

    @Transactional
    public Page<Patient> findPatientsByDoctorUsername(String username, Pageable pageable)  {
        return patientRepository.findPatientsByDoctor((Doctor) findUserByUsername(username), pageable);
    }

    @Transactional
    public List<Patient> findPatientsByDoctorUsername(String username)  {
        return patientRepository.findPatientsByDoctor((Doctor) findUserByUsername(username));
    }

    @Transactional
    public Page<Patient> findPatientsByNurseUsername(String username, Pageable pageable)  {
        return patientRepository.findPatientsByNurse((Nurse) findUserByUsername(username), pageable);
    }

    @Transactional
    public List<Patient> findPatientsByNurseUsername(String username)  {
        return patientRepository.findPatientsByNurse((Nurse) findUserByUsername(username));
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
    public Patient assignDoctorForPatient(Long patientId, Long doctorId) throws UserNotFoundException {
        Patient patient = (Patient) findById(patientId);
        Doctor doctor = (Doctor) findById(doctorId);
        patient.setDoctor(doctor);
        update(patient);
        LOGGER.info("Assigned doctor = {} for patient = {}", doctor, patient);
        return patient;
    }

    @Transactional
    public Patient assignNurseForPatient(Long patientId, Long nurseId) throws UserNotFoundException {
        Patient patient = (Patient) findById(patientId);
        Nurse nurse = (Nurse) findById(nurseId);
        patient.setNurse(nurse);
        update(patient);
        LOGGER.info("Assigned nurse = {} for patient = {}", nurse, patient);
        return patient;
    }

    @Transactional
    public Patient removeDoctorForPatient(Long id) throws UserNotFoundException {
        Patient patient = (Patient) findById(id);
        patient.removeDoctor();
        patient.removeNurse();
        update(patient);
        LOGGER.info("Removed doctor and nurse for patient = {}", patient);
        return patient;
    }

    @Transactional
    public Patient removeNurseForPatient(Long id) throws UserNotFoundException {
        Patient patient = (Patient) findById(id);
        patient.removeNurse();
        update(patient);
        LOGGER.info("Removed nurse for patient = {}", patient);
        return patient;
    }

    @Transactional
    public Patient createNoteForPatient(Long id, Note note, String doctorUsername) throws UserNotFoundException {
        note.setCreationDate(LocalDate.now());
        note.setDoctorIdWhoCreatedNote(findUserByUsername(doctorUsername).getId());
        noteService.save(note);
        Patient patient = (Patient) findById(id);
        patient.addMedicalNote(note);
        update(patient);
        LOGGER.info("Created note = {} for patient = {}", note, patient);
        return patient;
    }

    @Transactional
    public Note makeProcedureForPatient(Long patientId, Long noteId, String personWhoMakeProcedures) throws UserNotFoundException, NoteNotFoundException {
        Note note = noteService.findById(noteId);
        note.setPersonIdWhoMadeProcedures(findUserByUsername(personWhoMakeProcedures).getId());
        note.setProceduresDone(true);
        update(findById(patientId));
        return note;
    }

    @Transactional
    public Note makeSurgeryForPatient(Long patientId, Long noteId) throws UserNotFoundException, NoteNotFoundException {
        Note note = noteService.findById(noteId);
        note.setSurgeryDone(true);
        update(findById(patientId));
        return note;
    }
}
