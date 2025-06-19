package com.notes.dto;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatchNoteRequest {

    @Nullable
    private String title;

    @Nullable
    private String text;
}
