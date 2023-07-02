package ru.otus.filinovich.service.genre;

import ru.otus.filinovich.domain.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> getAllGenres();

    Genre getGenreByIdWithPromptAll();
}
