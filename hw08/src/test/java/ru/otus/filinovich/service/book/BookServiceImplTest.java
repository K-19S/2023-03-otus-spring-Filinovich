package ru.otus.filinovich.service.book;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.filinovich.dao.book.BookRepository;
import ru.otus.filinovich.domain.Author;
import ru.otus.filinovich.domain.Book;
import ru.otus.filinovich.domain.Genre;
import ru.otus.filinovich.service.author.AuthorService;
import ru.otus.filinovich.service.book.impl.BookServiceImpl;
import ru.otus.filinovich.service.genre.GenreService;
import ru.otus.filinovich.service.user.interaction.UserInteractionService;
import ru.otus.filinovich.util.MessageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Component
@ExtendWith(SpringExtension.class)
@Import(BookServiceImpl.class)
class BookServiceImplTest {

    private static final String CORRECT_BOOK_ID = "1";

    private static final String INCORRECT_BOOK_ID = "-100";

    private static List<Book> testBooks;

    @Autowired
    private BookServiceImpl bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserInteractionService userInteractionService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private MessageProvider messageProvider;

    @BeforeAll
    public static void initBooks() {
        testBooks = new ArrayList<>(2);
        testBooks.add(new Book("TestBook1", List.of(new Author("TestAuthor1")), new Genre("TestGenre1")));
        testBooks.add(new Book("TestBook2", List.of(new Author("TestAuthor2")), new Genre("TestGenre2")));
    }

    @Test
    public void getAllBooksTest() {
        List<Book> expected = testBooks;
        given(bookRepository.findAll()).willReturn(expected);
        List<Book> actual = bookService.getAllBooks();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getAllBooksDescriptionsTest() {
        List<Book> expectedList = testBooks;
        given(bookRepository.findAll()).willReturn(expectedList);
        StringBuilder builder = new StringBuilder();
        for (Book book : expectedList) {
            String authorName = book.getAuthors().get(0).getName();
            String genreName = book.getGenre().getName();
            builder.append("\"")
                    .append(book.getName()).append("\" ")
                    .append(authorName).append(", ")
                    .append(genreName).append(" | ID: ")
                    .append(book.getId()).append("\n");
        }
        String actual = bookService.getAllBooksDescriptions();
        assertThat(actual).isEqualTo(builder.toString());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void getBookByCorrectIdTest() {
        Book testBook = new Book("TestBook1", List.of(new Author("TestAuthor1")), new Genre("TestGenre1"));
        testBook.setId(CORRECT_BOOK_ID);
        given(bookRepository.findById(CORRECT_BOOK_ID)).willReturn(Optional.of(testBook));
        Book actual = bookService.getBookById(CORRECT_BOOK_ID);

        assertThat(testBook).isEqualTo(actual);
        verify(bookRepository, times(1)).findById(CORRECT_BOOK_ID);
    }

    @Test
    public void getBookByIncorrectIdTest() {
        given(bookRepository.findById(INCORRECT_BOOK_ID)).willReturn(Optional.empty());
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> bookService.getBookById(INCORRECT_BOOK_ID));
        verify(bookRepository, times(1)).findById(INCORRECT_BOOK_ID);
    }

    @Test
    public void getBookDescriptionByIdTest() {
        Book testBook = testBooks.get(0);
        given(bookRepository.findById(testBook.getId())).willReturn(Optional.of(testBook));
        String authorName = testBook.getAuthors().get(0).getName();
        String genreName = testBook.getGenre().getName();
        String expected = "\"" + testBook.getName() + "\" " + authorName + ", " + genreName;
        String actual = bookService.getBookDescriptionById(testBook.getId());

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findById(testBook.getId());
    }

    @Test
    public void getBookByIdWithPromptAllTest() {
        String prompt = "Test prompt";
        given(bookRepository.findAll()).willReturn(testBooks);
        given(userInteractionService.chooseBookFromList(testBooks)).willReturn(testBooks.get(0));

        Book actual = bookService.getBookByIdWithPromptAll(prompt);
        Book expected = testBooks.get(0);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findAll();
        verify(userInteractionService, times(1)).chooseBookFromList(testBooks);
    }

    @Test
    public void createNewBookTest() {
        Book testBook = testBooks.get(0);
        given(userInteractionService.getTextWithPrompt(any())).willReturn(testBook.getName());
        given(genreService.getGenreByIdWithPromptAll()).willReturn(testBook.getGenre());
        given(authorService.getAuthorByIdWithPromptAll()).willReturn(testBook.getAuthors().get(0));
        bookService.createNewBook();

        verify(userInteractionService, times(1)).getTextWithPrompt(any());
        verify(genreService, times(1)).getGenreByIdWithPromptAll();
        verify(authorService, times(1)).getAuthorByIdWithPromptAll();
        verify(bookRepository, times(1)).save(testBook);
    }

    @Test
    public void getDescriptionOfBookTest() {
        Book testBook = testBooks.get(0);
        String authorName = testBook.getAuthors().get(0).getName();
        String genreName = testBook.getGenre().getName();
        String expected = "\"" + testBook.getName() + "\" " + authorName + ", " + genreName;
        String actual = bookService.getDescriptionOfBook(testBook);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getDescriptionOfBookWithIdTest() {
        Book testBook = testBooks.get(0);
        String authorName = testBook.getAuthors().get(0).getName();
        String genreName = testBook.getGenre().getName();
        String expected = "\"" + testBook.getName() + "\" " + authorName + ", " + genreName
                + " | ID: " + testBook.getId();
        String actual = bookService.getDescriptionOfBookWithId(testBook);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateBookNameTest() {
        Book expectedBook = testBooks.get(0);

        given(messageProvider.getMessage("book.name")).willReturn("Name");
        given(messageProvider.getMessage("book.author")).willReturn("Author");
        given(messageProvider.getMessage("book.genre")).willReturn("Genre");
        given(userInteractionService.getLongWithPrompt(null)).willReturn(1L);
        String testName = "NewName";
        given(userInteractionService.getTextWithPrompt(anyString())).willReturn(testName);
        bookService.updateBook(expectedBook);
        expectedBook.setName(testName);

        verify(bookRepository, times(1)).save(expectedBook);
    }

    @Test
    public void updateBookAuthorTest() {
        Book expectedBook = testBooks.get(0);

        given(messageProvider.getMessage("book.name")).willReturn("Name");
        given(messageProvider.getMessage("book.author")).willReturn("Author");
        given(messageProvider.getMessage("book.genre")).willReturn("Genre");
        given(userInteractionService.getLongWithPrompt(null)).willReturn(2L);
        given(authorService.getAuthorByIdWithPromptAll()).willReturn(testBooks.get(1).getAuthors().get(0));
        bookService.updateBook(expectedBook);
        expectedBook.setAuthors(testBooks.get(1).getAuthors());

        verify(bookRepository, times(1)).save(expectedBook);
    }

    @Test
    public void updateBookGenreTest() {
        Book expectedBook = testBooks.get(0);

        given(messageProvider.getMessage("book.name")).willReturn("Name");
        given(messageProvider.getMessage("book.author")).willReturn("Author");
        given(messageProvider.getMessage("book.genre")).willReturn("Genre");
        given(userInteractionService.getLongWithPrompt(null)).willReturn(3L);
        given(genreService.getGenreByIdWithPromptAll()).willReturn(testBooks.get(1).getGenre());
        bookService.updateBook(expectedBook);
        expectedBook.setGenre(testBooks.get(1).getGenre());

        verify(bookRepository, times(1)).save(expectedBook);
    }

    @Test
    public void deleteByIdTest() {
        given(bookRepository.existsById(CORRECT_BOOK_ID)).willReturn(true);
        assertThat(bookService.deleteById(INCORRECT_BOOK_ID)).isFalse();
        verify(bookRepository, times(1)).existsById(INCORRECT_BOOK_ID);
        assertThat(bookService.deleteById(CORRECT_BOOK_ID)).isTrue();
        verify(bookRepository, times(1)).existsById(CORRECT_BOOK_ID);
        verify(bookRepository, times(1)).deleteById(any());
        verify(bookRepository, times(1)).deleteById(CORRECT_BOOK_ID);
    }
}