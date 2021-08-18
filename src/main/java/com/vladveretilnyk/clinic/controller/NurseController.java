package com.vladveretilnyk.clinic.controller;

import com.vladveretilnyk.clinic.controller.utility.SortUtility;
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
@RequestMapping("/nurse")
public class NurseController {

    private final UserService userService;

    private final NoteService noteService;

    public NurseController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping
    public String indexPage(@RequestParam(required = false) String sort, @RequestParam(required = false) String direction,
                            Pageable pageable, Model model) {
        model.addAttribute("page", userService.findPatientsByNurseUsername(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), SortUtility.getSort(sort, direction))
        ));

        model.addAttribute("url", "/doctor");
        model.addAttribute("sort", "sort=" + sort + "&direction=" + direction);

        return "nurse/index";
    }

    @GetMapping("/patients/{patientId}/medical-book")
    public String showMedicalBookPage(Model model, @PathVariable Long patientId,
                                      @RequestParam(required = false) String sort, @RequestParam(required = false) String direction,
                                      Pageable pageable) {

        model.addAttribute("patient", userService.findUserById(patientId));

        model.addAttribute("page", noteService.findNotesByPatientId(patientId,
                (PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), SortUtility.getSort(sort, direction)))));
        model.addAttribute("url", "/nurse/patients/" + patientId + "/medical-book");
        model.addAttribute("sort", "sort=" + sort + "&direction=" + direction);

        return "nurse/medical-book/notes";
    }

    @GetMapping("/patients/{patientId}/medical-book/note/{noteId}")
    public String showNote(Model model, @PathVariable Long noteId, @PathVariable Long patientId) {

        model.addAttribute("patient", userService.findUserById(patientId));

        model.addAttribute("note", noteService.findNoteById(noteId));

        return "nurse/medical-book/show";
    }

    @PostMapping("/patients/{patientId}/medical-book/note/{noteId}/procedures-done")
    public String proceduresPerformMedicalNote(@PathVariable Long noteId, @PathVariable Long patientId) {
        noteService.findNoteById(noteId).setProceduresDone(true);

        userService.update(userService.findUserById(patientId));

        return "redirect:/nurse/patients/{patientId}/medical-book/note/{noteId}";
    }
}
