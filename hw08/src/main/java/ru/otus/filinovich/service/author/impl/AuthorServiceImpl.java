package ru.otus.filinovich.service.author.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.filinovich.dao.author.AuthorRepository;
import ru.otus.filinovich.domain.Author;
import ru.otus.filinovich.service.author.AuthorService;
import ru.otus.filinovich.service.user.interaction.UserInteractionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final UserInteractionService userInteractionService;

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorByIdWithPromptAll() {
        Author chosenAuthor;
        do {
            chosenAuthor = userInteractionService.chooseAuthorFromList(getAllAuthors());
        } while (chosenAuthor == null);
        return chosenAuthor;
    }
}
