package ru.otus.filinovich.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.filinovich.dao.author.AuthorRepository;
import ru.otus.filinovich.dao.book.BookRepository;
import ru.otus.filinovich.dao.book_comments.BookCommentRepository;
import ru.otus.filinovich.dao.genre.GenreRepository;
import ru.otus.filinovich.domain.Author;
import ru.otus.filinovich.domain.Book;
import ru.otus.filinovich.domain.BookComment;
import ru.otus.filinovich.domain.Genre;

import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    private final List<Genre> genres = new ArrayList<>();

    private final List<Author> authors = new ArrayList<>();

    private List<Book> books = new ArrayList<>();

    private List<BookComment> comments = new ArrayList<>();

    @ChangeSet(order = "001", id = "dropDb", author = "s_filinovich", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertGenres", author = "s_filinovich")
    public void insertGenres(GenreRepository repository) {
        genres.add(new Genre("Mystery"));
        genres.add(new Genre("Thriller"));
        genres.add(new Genre("Horror"));
        genres.add(new Genre("Historical"));
        genres.add(new Genre("Western"));
        genres.add(new Genre("Fantasy"));
        repository.saveAll(genres);
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = "s_filinovich")
    public void insertGenres(AuthorRepository repository) {
        authors.add(new Author("Agatha Christie"));
        authors.add(new Author("James Patterson"));
        authors.add(new Author("Stephen King"));
        authors.add(new Author("Howard Lovecraft"));
        authors.add(new Author("Diana Gabaldon"));
        authors.add(new Author("Louis Lamour"));
        authors.add(new Author("George R. R. Martin"));
        authors.add(new Author("Joanne Rowling"));
        repository.saveAll(authors);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "s_filinovich")
    public void insertBooks(BookRepository repository) {
        books.add(new Book("Ten Little Niggers", List.of(authors.get(0)), genres.get(0)));
        books.add(new Book("The Murder House", List.of(authors.get(1)), genres.get(1)));
        books.add(new Book("IT", List.of(authors.get(2)), genres.get(2)));
        books.add(new Book("At the Mountains of Madness", List.of(authors.get(3)), genres.get(2)));
        books.add(new Book("Lord John and the Private Matter", List.of(authors.get(4)), genres.get(3)));
        books.add(new Book("Sackett", List.of(authors.get(5)), genres.get(4)));
        books.add(new Book("A Game of Thrones", List.of(authors.get(6)), genres.get(5)));
        books.add(new Book("Harry Potter and the Deathly Hallows", List.of(authors.get(7)), genres.get(5)));
        books = repository.saveAll(books);
    }

    @ChangeSet(order = "005", id="insertBookComments", author = "s_filinovich")
    public void insertBookComments(BookCommentRepository repository) {
        comments.add(new BookComment("Comment 1", books.get(0)));
        comments.add(new BookComment("Comment 2", books.get(1)));
        comments.add(new BookComment("Comment 3", books.get(2)));
        comments.add(new BookComment("Comment 4", books.get(3)));
        comments.add(new BookComment("Comment 5", books.get(4)));
        comments.add(new BookComment("Comment 6", books.get(5)));
        comments.add(new BookComment("Comment 7", books.get(6)));
        comments.add(new BookComment("Comment 8", books.get(7)));
        comments.add(new BookComment("Comment 9", books.get(7)));
        comments.add(new BookComment("Comment 10", books.get(7)));
        comments = repository.saveAll(comments);
    }
}
