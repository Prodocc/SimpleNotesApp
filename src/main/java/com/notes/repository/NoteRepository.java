package com.notes.repository;

import com.notes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findByTitle(String title);

    Optional<Note> findFirstByOrderByUpdatedAtDesc();

    void deleteByTitle(String title);

    boolean existsByTitle(String title);
}
