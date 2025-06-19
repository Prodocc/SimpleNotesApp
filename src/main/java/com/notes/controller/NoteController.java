package com.notes.controller;

import com.notes.dto.CreateNoteRequest;
import com.notes.dto.PatchNoteRequest;
import com.notes.dto.UpdateNoteRequest;
import com.notes.model.Note;
import com.notes.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    @ResponseBody
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{title}")
    public Note getNoteByTitle(@PathVariable String title) {
        return noteService.getNoteByTitle(title);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Note createNote(@Valid @RequestBody CreateNoteRequest request) {
        return noteService.createNote(request.getTitle(), request.getText());
    }

    @PutMapping("/{oldTitle}")
    public Note editNote(@PathVariable String oldTitle, @RequestBody UpdateNoteRequest request) {
        return noteService.editNoteByTitle(oldTitle, request.getTitle(), request.getText());
    }

    @PatchMapping("/{oldTitle}")
    public Note patchNote(@PathVariable String oldTitle, @RequestBody PatchNoteRequest request){
        return noteService.patchNoteByTitle(oldTitle, request.getTitle(), request.getText());
    }

    @DeleteMapping("/{title}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable String title) {
        noteService.deleteNoteByTitle(title);
    }
}
