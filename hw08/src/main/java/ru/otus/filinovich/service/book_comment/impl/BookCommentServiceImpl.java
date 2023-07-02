package ru.otus.filinovich.service.book_comment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.filinovich.dao.book_comments.BookCommentRepository;
import ru.otus.filinovich.domain.Book;
import ru.otus.filinovich.domain.BookComment;
import ru.otus.filinovich.service.book.BookService;
import ru.otus.filinovich.service.book_comment.BookCommentService;
import ru.otus.filinovich.service.user.interaction.UserInteractionService;
import ru.otus.filinovich.util.MessageProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

    private final BookCommentRepository bookCommentRepository;

    private final BookService bookService;

    private final UserInteractionService userInteractionService;

    private final MessageProvider messageProvider;

    @Override
    public List<BookComment> getAllCommentsByBookId(String id) {
        return bookCommentRepository.findByBookId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public String getAllCommentsDescriptionWithIdByBookId(String id) {
        List<BookComment> comments = getAllCommentsByBookId(id);
        StringBuilder builder = new StringBuilder();
        if (comments.isEmpty()) {
            return messageProvider.getMessage("comments._empty_list");
        }
        builder.append(comments.get(0).getBook().getName()).append('\n');
        comments.forEach(comment -> {
            builder.append(comment.getText()).append(", ID: ").append(comment.getId()).append("\n");
        });
        return builder.toString();
    }

    @Override
    public BookComment getCommentById(String id) {
        return bookCommentRepository.findById(id).orElseThrow();
    }

    @Override
    public String getCommentDescriptionById(String id) {
        BookComment comment = getCommentById(id);
        return getCommentDescription(comment);
    }

    @Override
    public String getCommentDescription(BookComment comment) {
        if (comment == null || comment.getBook() == null) {
            return messageProvider.getMessage("comments.null_comment");
        }
        return comment.getBook().getName() + ", " + comment.getText();
    }

    @Override
    public BookComment createComment() {
        Book selectedBook = chooseBookFromList();
        String commentText = getNewCommentText();
        BookComment newComment = new BookComment();
        newComment.setBook(selectedBook);
        newComment.setText(commentText);
        return bookCommentRepository.save(newComment);
    }

    @Override
    public BookComment updateComment() {
        String commentPrompt = messageProvider.getMessage("comments.select_comment_id");
        String commentId = userInteractionService.getTextWithPrompt(commentPrompt);
        BookComment comment = getCommentById(commentId);
        String commentText = getNewCommentText();
        comment.setText(commentText);
        return bookCommentRepository.save(comment);
    }

    @Override
    public String updateCommentAndGetDescription() {
        BookComment bookComment = updateComment();
        return getCommentDescription(bookComment);
    }

    @Override
    public boolean existsById(String id) {
        return bookCommentRepository.findById(id).isPresent();
    }

    @Override
    public boolean deleteById(String id) {
        if (existsById(id)) {
            bookCommentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    private String getNewCommentText() {
        String commentTextPrompt = messageProvider.getMessage("comments.enter_text");
        return userInteractionService.getTextWithPrompt(commentTextPrompt);
    }

    private Book chooseBookFromList() {
        String bookPrompt = messageProvider.getMessage("comments.select_book_for_commenting");
        return bookService.getBookByIdWithPromptAll(bookPrompt);
    }

}
