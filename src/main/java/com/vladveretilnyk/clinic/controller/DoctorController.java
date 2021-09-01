package com.vladveretilnyk.clinic.controller;

import com.vladveretilnyk.clinic.controller.utility.SortUtility;
import com.vladveretilnyk.clinic.entity.Note;
import com.vladveretilnyk.clinic.exception.NoteNotFoundException;
import com.vladveretilnyk.clinic.exception.UserNotFoundException;
import com.vladveretilnyk.clinic.service.NoteService;
import com.vladveretilnyk.clinic.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final UserService userService;

    private final NoteService noteService;

    public DoctorController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping
    public String indexPage(@RequestParam(name = "sort", required = false) String column, @RequestParam(required = false) String direction,
                            Pageable pageable, Model model)  {
        Sort sorting = SortUtility.getSort(column, direction);
        model.addAttribute("page", userService.findPatientsByDoctorUsername(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting)
        ));
        model.addAttribute("url", "/doctor");
        sorting.forEach(sortingTmp -> model.addAttribute("sort", "sort=" + sortingTmp.getProperty() + "&direction=" + sortingTmp.getDirection().name()));
        return "doctor/index";
    }

    // ASSIGN NURSE
    @GetMapping("/patients/{patientId}/assign/nurses")
    public String assignNursePage(@RequestParam(name = "sort", required = false) String column, @RequestParam(required = false) String direction, @PathVariable Long patientId, Pageable pageable, Model model) {
        Sort sorting = SortUtility.getSort(column, direction);
        model.addAttribute("id", patientId);
        model.addAttribute("page", userService.findAllNurses(
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting)
        ));
        model.addAttribute("url", "/doctor/patients/" + patientId + "/assign/nurses");
        sorting.forEach(sortingTmp -> model.addAttribute("sort", "sort=" + sortingTmp.getProperty() + "&direction=" + sortingTmp.getDirection().name()));
        return "doctor/assign-nurse";
    }

    @PostMapping("/patients/{patientId}/assign/nurses/{nurseId}")
    public String assignNurse(@PathVariable Long patientId, @PathVariable Long nurseId) throws UserNotFoundException {
        userService.assignNurseForPatient(patientId, nurseId);
        return "redirect:/doctor";
    }

    // REMOVE NURSE
    @PostMapping("/patients/{patientId}/remove-nurse")
    public String removeNurse(@PathVariable Long patientId) throws UserNotFoundException {
        userService.removeNurseForPatient(patientId);
        return "redirect:/doctor";
    }

    // MEDICAL BOOK
    @GetMapping("/patients/{patientId}/medical-book")
    public String medicalBookPage(@RequestParam(name = "sort", required = false) String column, @RequestParam(required = false) String direction,
                                  @PathVariable Long patientId, Pageable pageable, Model model) throws UserNotFoundException {
        Sort sorting = SortUtility.getSort(column, direction);
        model.addAttribute("patient", userService.findById(patientId));
        model.addAttribute("page", noteService.findNotesByPatientId(patientId,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting)
        ));
        model.addAttribute("url", "/doctor/patients/" + patientId + "/medical-book");
        sorting.forEach(sortingTmp -> model.addAttribute("sort", "sort=" + sortingTmp.getProperty() + "&direction=" + sortingTmp.getDirection().name()));
        return "doctor/medical-book/notes";
    }

    // NEW NOTE [GET]
    @GetMapping("/patients/{patientId}/medical-book/create-note")
    String newMedicalNotePage(@PathVariable(name = "patientId") Long id, Model model) throws UserNotFoundException {
        model.addAttribute("patient", userService.findById(id));
        model.addAttribute("note", new Note());
        return "doctor/medical-book/create";
    }

    // NEW NOTE [POST]
    @PostMapping("/patients/{patientId}/medical-book/create-note")
    public String createNote(@PathVariable Long patientId, @ModelAttribute Note note) throws UserNotFoundException {
        userService.createNoteForPatient(patientId, note,
                SecurityContextHolder.getContext().getAuthentication().getName());
        return "redirect:/doctor/patients/{patientId}/medical-book";
    }

    @GetMapping("/patients/{patientId}/medical-book/note/{noteId}")
    public String showNote(@PathVariable Long noteId, @PathVariable Long patientId, Model model) throws NoteNotFoundException, UserNotFoundException {
        if(noteService.findById(noteId).getPersonIdWhoMadeProcedures()!=null){
            model.addAttribute("executor", userService.findById(noteService.findById(noteId).getPersonIdWhoMadeProcedures()));
        }
        model.addAttribute("doctor", userService.findById(noteService.findById(noteId).getDoctorIdWhoCreatedNote()));
        model.addAttribute("patient", userService.findById(patientId));
        model.addAttribute("note", noteService.findById(noteId));
        return "doctor/medical-book/show";
    }


    @PostMapping("/patients/{patientId}/medical-book/note/{noteId}/procedures-done")
    public String proceduresPerformMedicalNote(@PathVariable Long noteId, @PathVariable Long patientId) throws UserNotFoundException, NoteNotFoundException {
        userService.makeProcedureForPatient(patientId, noteId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        return "redirect:/doctor/patients/{patientId}/medical-book/note/{noteId}";
    }

    @PostMapping("/patients/{patientId}")
    public String dischargePatient(@PathVariable Long patientId) throws UserNotFoundException {
        userService.removeDoctorForPatient(patientId);
        return "redirect:/doctor";
    }

    @PostMapping("/patients/{patientId}/medical-book/note/{noteId}/surgery-done")
    public String surgeryPerformMedicalNote(@PathVariable Long noteId, @PathVariable Long patientId) throws UserNotFoundException, NoteNotFoundException {
        userService.makeSurgeryForPatient(patientId, noteId);
        return "redirect:/doctor/patients/{patientId}/medical-book/note/{noteId}";
    }
}
