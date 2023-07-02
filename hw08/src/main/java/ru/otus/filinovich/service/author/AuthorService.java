package ru.otus.filinovich.service.author;

import ru.otus.filinovich.domain.Author;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();

    Author getAuthorByIdWithPromptAll();
}
