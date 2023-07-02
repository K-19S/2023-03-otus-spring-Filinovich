package ru.otus.filinovich.service.book_comment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.filinovich.dao.book_comments.BookCommentRepository;
import ru.otus.filinovich.domain.Author;
import ru.otus.filinovich.domain.Book;
import ru.otus.filinovich.domain.BookComment;
import ru.otus.filinovich.domain.Genre;
import ru.otus.filinovich.service.book.BookService;
import ru.otus.filinovich.service.book_comment.impl.BookCommentServiceImpl;
import ru.otus.filinovich.service.user.interaction.UserInteractionService;
import ru.otus.filinovich.util.MessageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Component
@ExtendWith(SpringExtension.class)
@Import(BookCommentServiceImpl.class)
class BookCommentServiceImplTest {

    private static List<BookComment> testComments;

    private static List<Book> testBooks;

    private static final String TEST_COMMENT_TEXT = "TestText1";

    private static final String CORRECT_COMMENT_ID = "1";

    private static final String INCORRECT_COMMENT_ID = "100";

    @Autowired
    private BookCommentServiceImpl bookCommentService;

    @MockBean
    private BookCommentRepository bookCommentRepository;

    @MockBean
    private BookService bookService;

    @MockBean
    private MessageProvider messageProvider;

    @MockBean
    private UserInteractionService userInteractionService;

    @BeforeAll
    public static void initComments() {
        testBooks = new ArrayList<>(2);
        Book book1 = new Book("TestBookName1",
                List.of(new Author("TestAuthor1")),
                new Genre("TestGenre1"));
        testBooks.add(book1);
        Book book2 = new Book("TestBookName2",
                List.of(new Author("TestAuthor2")),
                new Genre("TestGenre2"));
        testBooks.add(book2);
        testComments = new ArrayList<>(2);
        BookComment comment1 = new BookComment("TestText1", book1);
        testComments.add(comment1);
        BookComment comment2 = new BookComment("TestText2", book2);
        testComments.add(comment2);
    }

    @Test
    public void getAllCommentsByBookIdTest() {
        given(bookCommentRepository.findByBookId(testBooks.get(0).getId()))
                .willReturn(List.of(testComments.get(0), testComments.get(1)));
        List<BookComment> actual = bookCommentService.getAllCommentsByBookId(testBooks.get(0).getId());
        assertThat(actual).isEqualTo(List.of(testComments.get(0), testComments.get(1)));
        verify(bookCommentRepository, times(1)).findByBookId(testBooks.get(0).getId());
    }

    @Test
    public void getAllCommentsDescriptionWithIdByBookIdTest() {
        BookComment testComment = testComments.get(0);
        given(bookCommentRepository.findByBookId(testBooks.get(0).getId())).willReturn(List.of(testComment));
        String expected = testComment.getBook().getName() + "\n" +
                testComment.getText() + ", ID: " + testComment.getId() + "\n";

        String actual = bookCommentService.getAllCommentsDescriptionWithIdByBookId(testBooks.get(0).getId());
        assertThat(actual).isEqualTo(expected);
        verify(bookCommentRepository, times(1)).findByBookId(testBooks.get(0).getId());
    }

    @Test
    public void getCommentByIdTest() {
        BookComment testComment = testComments.get(0);
        given(bookCommentRepository.findById(testComment.getId())).willReturn(Optional.of(testComment));
        BookComment actual = bookCommentService.getCommentById(testComment.getId());
        assertThat(actual).isEqualTo(testComment);
        verify(bookCommentRepository, times(1)).findById(testComment.getId());
    }

    @Test
    public void getCommentDescriptionByIdTest() {
        BookComment testComment = testComments.get(0);
        given(bookCommentRepository.findById(testComment.getId())).willReturn(Optional.of(testComment));
        String actual = bookCommentService.getCommentDescriptionById(testComment.getId());
        String expected = testComment.getBook().getName() + ", " + testComment.getText();
        assertThat(actual).isEqualTo(expected);
        verify(bookCommentRepository, times(1)).findById(testComment.getId());
    }

    @Test
    public void getCommentDescriptionTest() {
        BookComment testComment = testComments.get(0);
        String actual = bookCommentService.getCommentDescription(testComment);
        String expected = testComment.getBook().getName() + ", " + testComment.getText();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void createCommentTest() {
        given(bookService.getBookByIdWithPromptAll(any())).willReturn(testBooks.get(0));
        given(userInteractionService.getTextWithPrompt(any())).willReturn(TEST_COMMENT_TEXT);
        BookComment comment = new BookComment();
        comment.setText(TEST_COMMENT_TEXT);
        comment.setBook(testBooks.get(0));
        bookCommentService.createComment();
        verify(bookService, times(1)).getBookByIdWithPromptAll(any());
        verify(userInteractionService, times(1)).getTextWithPrompt(any());
        verify(bookCommentRepository, times(1)).save(comment);
    }

    @Test
    public void updateCommentTest() {
        BookComment testComment = testComments.get(0);
        String testPrompt = "TestPrompt";
        given(bookCommentRepository.findById(CORRECT_COMMENT_ID)).willReturn(Optional.ofNullable(testComment));
        given(messageProvider.getMessage("comments.select_comment_id")).willReturn(testPrompt);
        given(userInteractionService.getTextWithPrompt(any())).willReturn(TEST_COMMENT_TEXT);
        given(userInteractionService.getTextWithPrompt(testPrompt)).willReturn(CORRECT_COMMENT_ID);
        if (testComment != null) {
            testComment.setText(TEST_COMMENT_TEXT);
            testComment.setBook(testBooks.get(0));
        }
        bookCommentService.updateComment();
        verify(bookCommentRepository, times(1)).findById(CORRECT_COMMENT_ID);
        verify(userInteractionService, times(1)).getTextWithPrompt(testPrompt);
        verify(userInteractionService, times(2)).getTextWithPrompt(any());
        verify(bookCommentRepository, times(1)).save(testComment);
    }

    @Test
    public void updateCommentAndGetDescriptionTest() {
        BookComment testComment = testComments.get(0);
        String testPrompt = "TestPrompt";
        given(bookCommentRepository.findById(CORRECT_COMMENT_ID)).willReturn(Optional.ofNullable(testComment));
        given(messageProvider.getMessage("comments.select_comment_id")).willReturn(testPrompt);
        given(userInteractionService.getTextWithPrompt(any())).willReturn(TEST_COMMENT_TEXT);
        given(userInteractionService.getTextWithPrompt(testPrompt)).willReturn(CORRECT_COMMENT_ID);
        given(bookCommentRepository.save(testComment)).willReturn(testComment);
        if (testComment != null) {
            testComment.setText(TEST_COMMENT_TEXT);
            testComment.setBook(testBooks.get(0));
        }
        String expected = testComment.getBook().getName() + ", " + testComment.getText();
        String actual = bookCommentService.updateCommentAndGetDescription();
        assertThat(actual).isEqualTo(expected);
        verify(bookCommentRepository, times(1)).findById(CORRECT_COMMENT_ID);
        verify(userInteractionService, times(1)).getTextWithPrompt(testPrompt);
        verify(userInteractionService, times(2)).getTextWithPrompt(any());
        verify(bookCommentRepository, times(1)).save(testComment);
    }

    @Test
    public void existsByIdTest() {
        given(bookCommentRepository.findById(CORRECT_COMMENT_ID)).willReturn(Optional.of(testComments.get(0)));
        assertThat(bookCommentService.existsById(CORRECT_COMMENT_ID)).isTrue();
        given(bookCommentRepository.findById(INCORRECT_COMMENT_ID)).willReturn(Optional.empty());
        assertThat(bookCommentService.existsById(INCORRECT_COMMENT_ID)).isFalse();
    }

    @Test
    public void deleteById() {
        given(bookCommentRepository.findById(CORRECT_COMMENT_ID)).willReturn(Optional.of(testComments.get(0)));
        boolean correctDeleted = bookCommentService.deleteById(CORRECT_COMMENT_ID);
        assertThat(correctDeleted).isTrue();
        verify(bookCommentRepository, times(1)).findById(CORRECT_COMMENT_ID);
        verify(bookCommentRepository, times(1)).deleteById(CORRECT_COMMENT_ID);
        given(bookCommentRepository.findById(INCORRECT_COMMENT_ID)).willReturn(Optional.empty());
        boolean incorrectDeleted = bookCommentService.deleteById(INCORRECT_COMMENT_ID);
        assertThat(incorrectDeleted).isFalse();
        verify(bookCommentRepository, times(1)).findById(INCORRECT_COMMENT_ID);
    }
}