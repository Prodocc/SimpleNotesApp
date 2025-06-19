package com.notes.service;

import com.notes.exception.NoteNotFoundException;
import com.notes.model.Note;
import com.notes.repository.NoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Test
    @DisplayName("Метод createNote должен вызывать noteRepository.save с корректно сформированным объёктом Note")
    void createNote_should_CallRepositorySaveWithCorrectNote() {
        String title = "title";
        String text = "text";

        ArgumentCaptor<Note> noteArgumentCaptor = ArgumentCaptor.forClass(Note.class);
        noteService.createNote(title, text);

        verify(noteRepository, times(1)).save(noteArgumentCaptor.capture());

        Note capturedNote = noteArgumentCaptor.getValue();
        assertThat(capturedNote).isNotNull();
        assertThat(capturedNote.getTitle()).isEqualTo(title);
        assertThat(capturedNote.getText()).isEqualTo(text);
    }

    @Test
    @DisplayName("getLastNote должен вернуть заметку, если репозиторий находит её")
    void getLastNote_whenNoteExists_shouldReturnNote() {
        Note expectedNote = new Note("Последняя заметка", "text1");

        when(noteRepository.findFirstByOrderByUpdatedAtDesc()).thenReturn(Optional.of(expectedNote));

        Note actualNote = noteService.getLastNote();

        assertThat(actualNote).isNotNull();
        assertThat(actualNote.getTitle()).isEqualTo("Последняя заметка");

        verify(noteRepository, times(1)).findFirstByOrderByUpdatedAtDesc();
    }

    @Test
    @DisplayName("getLastNote должен выбросить NoteNotFoundException, если репозиторий пустой")
    void getLastNote_whenNoteNotExists_shouldThrowException() {
        when(noteRepository.findFirstByOrderByUpdatedAtDesc()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.getLastNote())
                .isInstanceOf(NoteNotFoundException.class)
                .hasMessageContaining("В базе данных нет ни одной заметки");
    }

    @Test
    @DisplayName("Метод getAllNote должен вызывать noteRepository.findAll и возвращать верное количесто заметок")
    void getAllNotes_should_callRepositoryFindAll() {
        when(noteRepository.findAll()).thenReturn(List.of(new Note(), new Note()));

        List<Note> notesList = noteService.getAllNotes();

        assertThat(notesList).isNotNull();
        assertThat(notesList.size()).isEqualTo(2);

        verify(noteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getNoteByTitle должен вернуть заметку, если репозиторий находит её")
    void getNoteByTitle_whenNoteExists_shouldReturnNote() {
        Note expectedNote = new Note("title", "text");

        when(noteRepository.findByTitle("title")).thenReturn(Optional.of(expectedNote));

        Note actualNote = noteService.getNoteByTitle(expectedNote.getTitle());

        assertThat(actualNote).isNotNull();
        assertThat(actualNote.getTitle()).isEqualTo(expectedNote.getTitle());
        assertThat(actualNote.getText()).isEqualTo(expectedNote.getText());

        verify(noteRepository, times(1)).findByTitle(expectedNote.getTitle());
    }

    @Test
    @DisplayName("getNoteByTitle должен выбросить NoteNotFoundException, если заметки с таким названием нет")
    void getNoteByTitle_whenNoteNotExists_shouldThrowException() {
        String title = "title";
        when(noteRepository.findByTitle(title)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.getNoteByTitle(title)).isInstanceOf(NoteNotFoundException.class).hasMessageContaining("Заметка с названием " + title + " не найдена");
    }

    @Test
    @DisplayName("editNoteByTitle должен корректно изменять и сохранять новую версию заметку")
    void editNoteByTitle_whenNoteExists_shouldEditOldNoteCorrectly() {
        Note noteToUpdate = new Note("oldTitle", "oldText");
        String newTitle = "newTitle";
        String newText = "newText";

        when(noteRepository.findByTitle(noteToUpdate.getTitle())).thenReturn(Optional.of(noteToUpdate));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Note updatedNote = noteService.editNoteByTitle(noteToUpdate.getTitle(), newTitle, newText);

        assertThat(updatedNote).isNotNull();
        assertThat(updatedNote.getTitle()).isEqualTo(newTitle);
        assertThat(updatedNote.getText()).isEqualTo(newText);

        ArgumentCaptor<Note> noteCaptor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).save(noteCaptor.capture());

        Note savedNote = noteCaptor.getValue();
        assertThat(savedNote.getTitle()).isEqualTo(newTitle);
        assertThat(savedNote.getText()).isEqualTo(newText);
    }

    @Test
    @DisplayName("editNoteByTitle должен выбросить NoteNotFoundException, если заметки с таким названием нет")
    void editNoteByTitle_whenNoteNotExists_shouldThrowsException() {
        String oldTitle = "oldTitle";
        String newTitle = "newTitle";
        String newText = "newText";

        when(noteRepository.findByTitle(oldTitle)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> noteService.editNoteByTitle(oldTitle, newTitle, newText)).isInstanceOf(NoteNotFoundException.class).hasMessageContaining("Заметка с названием " + oldTitle + " не найдена");
    }

    @Test
    @DisplayName("deleteNoteByTitle должен удалять заметку, если она существует")
    void deleteNoteByTitle_whenNoteExist_shouldDeleteNoteCorrectly() {
        String title = "title";

        when(noteRepository.existsByTitle(title)).thenReturn(true);

        noteService.deleteNoteByTitle(title);

        verify(noteRepository, times(1)).deleteByTitle(title);
    }

    @Test
    @DisplayName("deleteNoteByTitle должен выбрасывать NoteNotFoundException, если такой заметки нет")
    void deleteNoteByTitle_whenNoteNotExist_shouldThrowException() {
        String title = "title";

        when(noteRepository.existsByTitle(title)).thenReturn(false);

        assertThatThrownBy(() -> noteService.deleteNoteByTitle(title)).isInstanceOf(NoteNotFoundException.class).hasMessageContaining("Невозможно удалить. Заметка с названием " + title + " не найдена.");
    }

}