package com.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notes.dto.CreateNoteRequest;
import com.notes.dto.UpdateNoteRequest;
import com.notes.exception.NoteNotFoundException;
import com.notes.model.Note;
import com.notes.service.NoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/notes должен вернуть список всех заметок и статус 200 OK")
    public void getAllNotes_shouldReturnListOfNotes() throws Exception {
        Note note1 = new Note("title1", "text1");
        Note note2 = new Note("title2", "text2");

        when(noteService.getAllNotes()).thenReturn(List.of(note1, note2));

        mvc.perform(get("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("title1")))
                .andExpect(jsonPath("$[0].text", is("text1")))
                .andExpect(jsonPath("$[1].title", is("title2")))
                .andExpect(jsonPath("$[1].text", is("text2")));

        verify(noteService, times(1)).getAllNotes();
    }

    @Test
    @DisplayName("GET /api/notes/{title} должен вернуть заметку с именем - title и статус 200 OK")
    public void getNoteByTitle_whenNoteExists_ShouldReturnNote() throws Exception {
        Note note = createTestNote();

        when(noteService.getNoteByTitle(note.getTitle())).thenReturn(note);

        mvc.perform(get("/api/notes/{title}", note.getTitle())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(note.getTitle())))
                .andExpect(jsonPath("$.text", is(note.getText())));

        verify(noteService, times(1)).getNoteByTitle(note.getTitle());
    }

    @Test
    @DisplayName("GET /api/notes/{title} должен вернуть статус 404 Not Found")
    public void getNoteByTitle_whenNoteNotExists_ShouldReturnNotFound() throws Exception {
        String title = "title1";

        when(noteService.getNoteByTitle(title)).thenThrow(new NoteNotFoundException("Заметка с названием " + title + " не найдена."));

        mvc.perform(get("/api/notes/{title}", title)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(noteService, times(1)).getNoteByTitle(title);
    }

    @Test
    @DisplayName("POST /api/notes должен вернуть созданный объект и статус 201 CREATED")
    public void createNote_whenRequestIsValid_shouldCreateAndReturnNote() throws Exception {
        String title = "title1";
        String text = "text1";

        CreateNoteRequest createRequest = new CreateNoteRequest();
        createRequest.setTitle(title);
        createRequest.setText(text);

        Note returnedNote = new Note(title, text);

        when(noteService.createNote(title, text)).thenReturn(returnedNote);

        mvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.text", is(text)));

        verify(noteService, times(1)).createNote(title, text);
    }

    @Test
    @DisplayName("POST /api/notes с невалидными данными должен вернуть код 400 Bad Request")
    public void createNote_whenRequestIsInvalid_shouldReturnBadRequest() throws Exception {
        String title = "";
        String text = "text1";

        CreateNoteRequest createRequest = new CreateNoteRequest();
        createRequest.setTitle(title);
        createRequest.setText(text);

        mvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(noteService, never()).createNote(any(), any());
    }

    @Test
    @DisplayName("PUT /api/notes/{oldTitle} должен возвращать изменённый объект и статус 200 ОК")
    public void editNote_whenNoteExists_shouldUpdateAndReturnNote() throws Exception {
        String oldTitle = "oldTitle";
        String newTitle = "newTitle";
        String newText = "newText";

        UpdateNoteRequest updateRequest = new UpdateNoteRequest();
        updateRequest.setTitle(newTitle);
        updateRequest.setText(newText);

        Note returnedNote = new Note(newTitle, newText);

        when(noteService.editNoteByTitle(oldTitle, newTitle, newText)).thenReturn(returnedNote);

        mvc.perform(put("/api/notes/{oldTitle}", oldTitle)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(newTitle)))
                .andExpect(jsonPath("$.text", is(newText)));

        verify(noteService, times(1)).editNoteByTitle(oldTitle, newTitle, newText);
    }

    @Test
    @DisplayName("PUT /api/notes/{oldTitle} должен возвращать статус 404 Not Found")
    public void editNote_whenNoteNoteExists_shouldReturnNotFound() throws Exception {
        String oldTitle = "oldTitle";
        String newTitle = "newTitle";
        String newText = "newText";

        UpdateNoteRequest updateRequest = new UpdateNoteRequest();
        updateRequest.setTitle(newTitle);
        updateRequest.setText(newText);

        when(noteService.editNoteByTitle(oldTitle, newTitle, newText))
                .thenThrow(new NoteNotFoundException("Заметка с названием " + oldTitle + " не найдена."));

        mvc.perform(put("/api/notes/{oldTitle}", oldTitle)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(noteService, times(1)).editNoteByTitle(oldTitle, newTitle, newText);
    }

    @Test
    @DisplayName("DELETE /api/notes/{title} должен удалять заметку и вернуть статус 204 No Content")
    public void deleteNote_whenNoteExists_shouldReturnNoContent() throws Exception {
        String title = "title";

        doNothing().when(noteService).deleteNoteByTitle(title);

        mvc.perform(delete("/api/notes/{title}", title))
                .andExpect(status().isNoContent());

        verify(noteService, times(1)).deleteNoteByTitle(title);
    }

    @Test
    @DisplayName("DELETE /api/notes/{title} должен возвращать 404 Not Found")
    public void deleteNote_whenNoteNotExists_shouldReturnNotFound() throws Exception {
        String title = "title";

        doThrow(new NoteNotFoundException("Невозможно удалить. Заметка с названием " + title + " не найдена."))
                .when(noteService)
                .deleteNoteByTitle(title);

        mvc.perform(delete("/api/notes/{title}", title))
                .andExpect(status().isNotFound());
    }

    private CreateNoteRequest createValidCreateRequest() {
        CreateNoteRequest request = new CreateNoteRequest();
        request.setTitle("valid-title");
        request.setText("Valid-text");
        return request;
    }

    private Note createTestNote() {
        return new Note("test-title", "text-text");
    }
}