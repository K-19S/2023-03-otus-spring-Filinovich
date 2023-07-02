package ru.otus.filinovich.service.book_comment;

import ru.otus.filinovich.domain.BookComment;

import java.util.List;

public interface BookCommentService {

    List<BookComment> getAllCommentsByBookId(String id);

    String getAllCommentsDescriptionWithIdByBookId(String id);

    BookComment getCommentById(String id);

    String getCommentDescriptionById(String id);

    String getCommentDescription(BookComment bookComment);

    BookComment createComment();

    BookComment updateComment();

    String updateCommentAndGetDescription();

    boolean existsById(String id);

    boolean deleteById(String id);
}
