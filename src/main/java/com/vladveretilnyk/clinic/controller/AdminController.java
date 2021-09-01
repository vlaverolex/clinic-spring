package com.vladveretilnyk.clinic.controller;

import com.vladveretilnyk.clinic.controller.utility.SortUtility;
import com.vladveretilnyk.clinic.entity.Doctor;
import com.vladveretilnyk.clinic.entity.Nurse;
import com.vladveretilnyk.clinic.entity.Patient;
import com.vladveretilnyk.clinic.exception.UserNotFoundException;
import com.vladveretilnyk.clinic.service.CategoryService;
import com.vladveretilnyk.clinic.service.UserService;
import com.vladveretilnyk.clinic.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final CategoryService categoryService;

    private final UserValidator validator;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private static final LocalDate MAX_DATE_FOR_DOCTOR = LocalDate.now().minusYears(25);

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private static final LocalDate MAX_DATE_FOR_NURSE = LocalDate.now().minusYears(18);

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private static final LocalDate MAX_DATE_FOR_PATIENT = LocalDate.now().minusYears(1);

    private final static Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    public AdminController(UserService userService, CategoryService categoryService, UserValidator validator) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.validator = validator;
    }

    @GetMapping
    public String indexPage() {
        return "admin/index";
    }


    // SHOW ALL
    @GetMapping("/doctors")
    public String showAllDoctors(@RequestParam(name = "sort", required = false) String column, @RequestParam(required = false) String direction,
                                 Pageable pageable, Model model) {
        Sort sorting = SortUtility.getSort(column, direction);
        model.addAttribute("page", userService.findAllDoctors(
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting)
        ));
        model.addAttribute("url", "/admin/doctors");
        sorting.forEach(sortingTmp -> model.addAttribute("sort", "sort=" + sortingTmp.getProperty() + "&direction=" + sortingTmp.getDirection().name()));
        return "admin/doctors/show";
    }

    @GetMapping("/nurses")
    public String showAllNurses(@RequestParam(name = "sort", required = false) String column, @RequestParam(required = false) String direction,
                                Pageable pageable, Model model) {
        Sort sorting = SortUtility.getSort(column, direction);
        model.addAttribute("page", userService.findAllNurses(
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting)
        ));
        model.addAttribute("url", "/admin/nurses");
        sorting.forEach(sortingTmp -> model.addAttribute("sort", "sort=" + sortingTmp.getProperty() + "&direction=" + sortingTmp.getDirection().name()));
        return "admin/nurses/show";
    }

    @GetMapping("/patients")
    public String showAllPatients(@RequestParam(name = "sort", required = false) String column, @RequestParam(required = false) String direction,
                                  Pageable pageable, Model model) {
        Sort sorting = SortUtility.getSort(column, direction);
        model.addAttribute("page", userService.findAllPatients(
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting)
        ));
        model.addAttribute("url", "/admin/patients");
        sorting.forEach(sortingTmp -> model.addAttribute("sort", "sort=" + sortingTmp.getProperty() + "&direction=" + sortingTmp.getDirection().name()));
        return "admin/patients/show";
    }


    // CREATE [GET]
    @GetMapping("/doctors/create")
    public String showDoctorCreationPage(Model model) {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("maxDate", MAX_DATE_FOR_DOCTOR);
        return "admin/doctors/create";
    }

    @GetMapping("/nurses/create")
    public String showNurseCreationPage(Model model) {
        model.addAttribute("nurse", new Nurse());
        model.addAttribute("maxDate", MAX_DATE_FOR_NURSE);
        return "admin/nurses/create";
    }

    @GetMapping("/patients/create")
    public String showPatientCreationPage(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("categoryDoctors", categoryService.findCategoryDoctors());
        model.addAttribute("maxDate", MAX_DATE_FOR_PATIENT);
        return "admin/patients/create";
    }


    // CREATE [POST]
    @PostMapping("/doctors/create")
    public String saveNewDoctor(@ModelAttribute Doctor doctor, @RequestParam String categoryName,
                                BindingResult bindingResult, Model model) {
        validator.validate(doctor, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("maxDate", MAX_DATE_FOR_DOCTOR);
            return "admin/doctors/create";
        }
        doctor.setCategory(categoryService.findByName(categoryName));
        try {
            userService.save(doctor);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("User already exist, username = {}", doctor.getUsername());
            bindingResult.rejectValue("username", "duplicate.user.username");
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("maxDate", MAX_DATE_FOR_DOCTOR);
            return "admin/doctors/create";
        }
        return "redirect:/admin/doctors";
    }

    @PostMapping("/nurses/create")
    public String saveNewNurse(@ModelAttribute Nurse nurse,
                               BindingResult bindingResult, Model model) {
        validator.validate(nurse, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("maxDate", MAX_DATE_FOR_NURSE);
            return "admin/nurses/create";
        }
        try {
            userService.save(nurse);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("User already exist, username = {}", nurse.getUsername());
            bindingResult.rejectValue("username", "duplicate.user.username");
            model.addAttribute("maxDate", MAX_DATE_FOR_NURSE);
            return "admin/nurses/create";
        }
        return "redirect:/admin/nurses";
    }

    @PostMapping("/patients/create")
    public String saveNewUser(@ModelAttribute Patient patient, @RequestParam String doctorUsername,
                              BindingResult bindingResult, Model model) {
        validator.validate(patient, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryDoctors", categoryService.findCategoryDoctors());
            model.addAttribute("maxDate", MAX_DATE_FOR_PATIENT);
            return "admin/patients/create";
        }
        patient.setDoctor((Doctor) userService.findUserByUsername(doctorUsername));
        try {
            userService.save(patient);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("User already exist, username = {}", patient.getUsername());
            bindingResult.rejectValue("username", "duplicate.user.username");
            model.addAttribute("categoryDoctors", categoryService.findCategoryDoctors());
            model.addAttribute("maxDate", MAX_DATE_FOR_PATIENT);
            return "admin/patients/create";
        }
        return "redirect:/admin/patients";
    }


    // ASSIGN / REMOVE DOCTOR FOR PATIENT
    @GetMapping("/patients/{id}/assign-doctor")
    public String assignDoctorPage(Model model, @PathVariable Long id,
                                   @RequestParam(name = "sort", required = false) String column, @RequestParam(required = false) String direction,
                                   Pageable pageable) throws UserNotFoundException {
        Sort sorting = SortUtility.getSort(column, direction);
        Patient patient;
        patient = (Patient) userService.findById(id);
        model.addAttribute("patient", patient);
        model.addAttribute("page", userService.findAllDoctors(
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting)
        ));
        model.addAttribute("url", "/admin/patients/" + patient.getId() + "/assign-doctor");
        sorting.forEach(sortingTmp -> model.addAttribute("sort", "sort=" + sortingTmp.getProperty() + "&direction=" + sortingTmp.getDirection().name()));
        return "admin/patients/assign-doctor";
    }

    @PostMapping("/patients/{patientId}/add-doctor/{doctorId}")
    public String assignDoctor(@PathVariable Long patientId,
                               @PathVariable Long doctorId) throws UserNotFoundException {
        userService.assignDoctorForPatient(patientId, doctorId);
        return "redirect:/admin/patients";
    }

    @PostMapping("/patients/{patientId}/remove-doctor")
    public String removeDoctor(@PathVariable Long patientId) throws UserNotFoundException {
        userService.removeDoctorForPatient(patientId);
        return "redirect:/admin/patients";
    }


    // DELETE PATIENT
    @PostMapping("/patients/{patientId}")
    public String deletePatient(@PathVariable Long patientId) throws UserNotFoundException {
        userService.removeDoctorForPatient(patientId);
        userService.removeNurseForPatient(patientId);
        return "redirect:/admin/patients";
    }
}
