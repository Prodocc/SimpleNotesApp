package com.notes.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
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
}
