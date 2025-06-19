package com.notes.service;

import com.notes.exception.NoteNotFoundException;
import com.notes.model.Note;
import com.notes.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Note getLastNote() {
        return noteRepository.findFirstByOrderByUpdatedAtDesc()
                .orElseThrow(() -> new NoteNotFoundException("В базе данных нет ни одной заметки."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    @Transactional
    public Note createNote(String title, String text) {
        Note note = new Note();
        note.setTitle(title);
        note.setText(text);

        return noteRepository.save(note);
    }

    @Override
    @Transactional(readOnly = true)
    public Note getNoteByTitle(String title) {
        return noteRepository.findByTitle(title)
                .orElseThrow(() -> new NoteNotFoundException("Заметка с названием " + title + " не найдена."));
    }

    @Override
    @Transactional
    public Note editNoteByTitle(String oldTitle, String newTitle, String newText) {
        Note noteToUpdate = noteRepository.findByTitle(oldTitle).orElseThrow(() -> new NoteNotFoundException("Заметка с названием " + oldTitle + " не найдена"));
        noteToUpdate.setTitle(newTitle);
        noteToUpdate.setText(newText);
        noteRepository.save(noteToUpdate);
        return noteToUpdate;
    }

    @Override
    @Transactional
    public void deleteNoteByTitle(String title) {
        if (noteRepository.existsByTitle(title)) {
            noteRepository.deleteByTitle(title);
        } else {
            throw new NoteNotFoundException("Невозможно удалить. Заметка с названием " + title + " не найдена.");
        }
    }
}
