package com.vladveretilnyk.clinic.controller;

import com.vladveretilnyk.clinic.controller.utility.SortUtility;
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

@Controller
@RequestMapping("/nurse")
public class NurseController {

    private final UserService userService;

    private final NoteService noteService;

    public NurseController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping
    public String indexPage(@RequestParam(name = "sort", required = false) String column, @RequestParam(required = false) String direction,
                            Pageable pageable, Model model) {
        Sort sorting = SortUtility.getSort(column, direction);
        model.addAttribute("page", userService.findPatientsByNurseUsername(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting)
        ));
        model.addAttribute("url", "/doctor");
        sorting.forEach(sortingTmp -> model.addAttribute("sort", "sort=" + sortingTmp.getProperty() + "&direction=" + sortingTmp.getDirection().name()));
        return "nurse/index";
    }

    @GetMapping("/patients/{patientId}/medical-book")
    public String showMedicalBookPage(Model model, @PathVariable Long patientId,
                                      @RequestParam(name = "sort", required = false) String column, @RequestParam(required = false) String direction,
                                      Pageable pageable) throws UserNotFoundException {
        Sort sorting = SortUtility.getSort(column, direction);
        model.addAttribute("patient", userService.findById(patientId));
        model.addAttribute("page", noteService.findNotesByPatientId(patientId,
                (PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting))));
        model.addAttribute("url", "/nurse/patients/" + patientId + "/medical-book");
        sorting.forEach(sortingTmp -> model.addAttribute("sort", "sort=" + sortingTmp.getProperty() + "&direction=" + sortingTmp.getDirection().name()));
        return "nurse/medical-book/notes";
    }

    @GetMapping("/patients/{patientId}/medical-book/note/{noteId}")
    public String showNote(Model model, @PathVariable Long noteId, @PathVariable Long patientId) throws NoteNotFoundException, UserNotFoundException {
        if (noteService.findById(noteId).getPersonIdWhoMadeProcedures() != null) {
            model.addAttribute("executor", userService.findById(noteService.findById(noteId).getPersonIdWhoMadeProcedures()));
        }
        model.addAttribute("doctor", userService.findById(noteService.findById(noteId).getDoctorIdWhoCreatedNote()));
        model.addAttribute("patient", userService.findById(patientId));
        model.addAttribute("note", noteService.findById(noteId));
        return "nurse/medical-book/show";
    }

    @PostMapping("/patients/{patientId}/medical-book/note/{noteId}/procedures-done")
    public String proceduresPerformMedicalNote(@PathVariable Long noteId, @PathVariable Long patientId) throws UserNotFoundException, NoteNotFoundException {
        userService.makeProcedureForPatient(patientId, noteId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        return "redirect:/nurse/patients/{patientId}/medical-book/note/{noteId}";
    }
}
