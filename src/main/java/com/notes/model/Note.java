package com.notes.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "NOTES")
public class Note extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "TITLE", unique = true, nullable = false, length = 40)
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String text;

    public Note() {

    }

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Note note = (Note) obj;
        return id != 0 && id == note.getId();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
