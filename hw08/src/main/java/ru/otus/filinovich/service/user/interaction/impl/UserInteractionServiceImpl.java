package ru.otus.filinovich.service.user.interaction.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.filinovich.domain.Author;
import ru.otus.filinovich.domain.Book;
import ru.otus.filinovich.domain.Genre;
import ru.otus.filinovich.service.io.IOService;
import ru.otus.filinovich.service.user.interaction.UserInteractionService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class UserInteractionServiceImpl implements UserInteractionService {

    private final IOService ioService;

    @Override
    public void showText(String text) {
        ioService.outputString(text);
    }

    @Override
    public String getText() {
        return ioService.readString();
    }

    @Override
    public Long getLong() {
        return ioService.readLong();
    }

    @Override
    public String getTextWithPrompt(String promt) {
        return ioService.readStringWithPrompt(promt);
    }

    @Override
    public Long getLongWithPrompt(String promt) {
        return ioService.readLongWithPrompt(promt);
    }

    @Override
    public Book chooseBookFromList(List<Book> allBooks) {
        AtomicInteger count = new AtomicInteger(1);
        allBooks.forEach(book -> {
            String bookDescription =
                    book.getName() + "\" " + book.getAuthors().get(0).getName() + ", " + book.getGenre().getName();
            showText(count.getAndIncrement() + ". " + bookDescription);
        });
        Integer bookNumber = ioService.readInteger(0, allBooks.size());
        return allBooks.get(bookNumber - 1);
    }

    @Override
    public Genre chooseGenreFromList(List<Genre> allGenres) {
        AtomicInteger count = new AtomicInteger(1);
        allGenres.forEach(genre -> showText(count.getAndIncrement() + ". " + genre.getName()));
        Integer genreNumber = ioService.readInteger(0, allGenres.size());
        return allGenres.get(genreNumber - 1);
    }

    @Override
    public Author chooseAuthorFromList(List<Author> allAuthors) {
        AtomicInteger count = new AtomicInteger(1);
        allAuthors.forEach(author -> showText(count.getAndIncrement() + ". " + author.getName()));
        Integer authorNumber = ioService.readInteger(0, allAuthors.size());
        return allAuthors.get(authorNumber - 1);
    }
}

