package com.vladveretilnyk.clinic.controller;

import com.vladveretilnyk.clinic.controller.utility.SortUtility;
import com.vladveretilnyk.clinic.entity.Note;
import com.vladveretilnyk.clinic.service.NoteService;
import com.vladveretilnyk.clinic.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String indexPage(@RequestParam(required = false) String sort, @RequestParam(required = false) String direction,
                             Pageable pageable, Model model) {

            model.addAttribute("page", userService.findPatientsByDoctorUsername(
                    SecurityContextHolder.getContext().getAuthentication().getName(),

                    PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), SortUtility.getSort(sort, direction))
            ));

        model.addAttribute("url", "/doctor");
        model.addAttribute("sort", "sort=" + sort + "&direction=" + direction);

        return "doctor/index";
    }

    // ASSIGN NURSE
    @GetMapping("/patients/{patientId}/assign/nurses")
    public String assignNursePage(@RequestParam(required = false) String sort, @RequestParam(required = false) String direction,
                                  @PathVariable Long patientId, Pageable pageable, Model model) {
        model.addAttribute("id", patientId);
        model.addAttribute("page", userService.findAllNurses(
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), SortUtility.getSort(sort, direction))
        ));
        model.addAttribute("url", "/doctor/patients/" + patientId + "/assign/nurses");
        model.addAttribute("sort", "sort=" + sort + "&direction=" + direction);

        return "doctor/assign-nurse";
    }

    @PostMapping("/patients/{patientId}/assign/nurses/{nurseId}")
    public String assignNurse(@PathVariable Long patientId, @PathVariable Long nurseId) {

            userService.assignNurseForPatient(patientId, nurseId);

        return "redirect:/doctor";
    }

    // REMOVE NURSE
    @PostMapping("/patients/{patientId}/remove-nurse")
    public String removeNurse(@PathVariable Long patientId) {

            userService.removeNurseForPatient(patientId);

        return "redirect:/doctor";
    }

    // MEDICAL BOOK
    @GetMapping("/patients/{patientId}/medical-book")
    public String medicalBookPage(@RequestParam(required = false) String sort, @RequestParam(required = false) String direction,
                                  @PathVariable Long patientId, Pageable pageable, Model model) {

            model.addAttribute("patient", userService.findUserById(patientId));

        model.addAttribute("page", noteService.findNotesByPatientId(patientId,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), SortUtility.getSort(sort, direction))
        ));
        model.addAttribute("url", "/doctor/patients/" + patientId + "/medical-book");
        model.addAttribute("sort", "sort=" + sort + "&direction=" + direction);

        return "doctor/medical-book/notes";
    }

    // NEW NOTE [GET]
    @GetMapping("/patients/{patientId}/medical-book/create-note")
    String newMedicalNotePage(@PathVariable(name = "patientId") Long id, Model model) {

            model.addAttribute("patient", userService.findUserById(id));


        model.addAttribute("note", new Note());

        return "doctor/medical-book/create";
    }

    // NEW NOTE [POST]
    @PostMapping("/patients/{patientId}/medical-book/create-note")
    public String createNote(@PathVariable Long patientId, @ModelAttribute Note note) {

            userService.createNoteForPatient(patientId, note);

        return "redirect:/doctor/patients/{patientId}/medical-book";
    }

    @GetMapping("/patients/{patientId}/medical-book/note/{noteId}")
    public String showNote(@PathVariable Long noteId, @PathVariable Long patientId, Model model) {

            model.addAttribute("patient", userService.findUserById(patientId));


        model.addAttribute("note", noteService.findNoteById(noteId));
        return "doctor/medical-book/show";
    }


    @PostMapping("/patients/{patientId}/medical-book/note/{noteId}/procedures-done")
    public String proceduresPerformMedicalNote(@PathVariable Long noteId, @PathVariable Long patientId) {
        noteService.findNoteById(noteId).setProceduresDone(true);

            userService.update(userService.findUserById(patientId));

        return "redirect:/doctor/patients/{patientId}/medical-book/note/{noteId}";
    }

    @PostMapping("/patients/{patientId}/medical-book/note/{noteId}/surgery-done")
    public String surgeryPerformMedicalNote(@PathVariable Long noteId, @PathVariable Long patientId) {
        noteService.findNoteById(noteId).setSurgeryDone(true);

            userService.update(userService.findUserById(patientId));

        return "redirect:/doctor/patients/{patientId}/medical-book/note/{noteId}";
    }
}
