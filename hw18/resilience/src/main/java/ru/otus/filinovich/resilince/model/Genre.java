package ru.otus.filinovich.resilince.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    private String id;

    private String name;

    public Genre(String name) {
        this.name = name;
    }
}
