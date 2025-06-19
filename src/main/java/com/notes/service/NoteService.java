package com.notes.service;

import com.notes.model.Note;

import java.util.List;

public interface NoteService {

    Note getLastNote();

    List<Note> getAllNotes();

    Note createNote(String title, String text);
    Note getNoteByTitle(String title);

    Note editNoteByTitle(String oldTitle, String newTitle, String newText);

    void deleteNoteByTitle(String title);
}
