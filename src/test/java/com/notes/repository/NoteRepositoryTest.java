package com.notes.repository;

import com.notes.model.Note;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void saveNote_thenFindByTitle() {
        Note note = new Note();
        note.setTitle("Note1");
        note.setText("text1");

        noteRepository.save(note);

        Note byTitle = noteRepository.findByTitle(note.getTitle()).orElseThrow();
        assertEquals(note, byTitle);
    }

    @Test
    @DisplayName("После сохранения новой заметки она должна быть последней по времени обновления")
    void whenSaveNewNote_thenItShouldBeTheLastUpdated() {
        // Arrange
        Note note = new Note();
        note.setTitle("Note1");
        note.setText("text1");

        // Act
        noteRepository.save(note);

        // Assert
        Note lastNote = noteRepository.findFirstByOrderByUpdatedAtDesc().orElseThrow();

        assertNotNull(lastNote.getUpdatedAt());
        assertEquals(note.getTitle(), lastNote.getTitle());
        assertEquals(note.getText(), lastNote.getText());
    }

    @Test
    @DisplayName("Поиск последней заметки в пустой таблице должен вернуть пустой Optional")
    void whenTableIsEmpty_findLatest_shouldReturnEmpty() {
        assertThat(noteRepository.findFirstByOrderByUpdatedAtDesc()).isEmpty();
    }

    @Test
    @DisplayName("Метод findAll() должен возвращать список всех сохранённых заметок")
    void whenMultipleNotesAreSaved_findAll_ShouldReturnListWithSameSize() {
        int size = 3;
        saveTestNotes(3);

        List<Note> allNotes = noteRepository.findAll();

        assertEquals(size, allNotes.size());
    }

    private List<Note> saveTestNotes(int count) {
        List<Note> savedNotes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Note tmpNote = new Note();
            tmpNote.setTitle("title " + i);
            tmpNote.setText("text " + i);
            savedNotes.add(noteRepository.save(tmpNote));
        }
        return savedNotes;
    }
}