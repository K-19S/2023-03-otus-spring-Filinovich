package ru.otus.filinovich.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.filinovich.domain.BookComment;
import ru.otus.filinovich.service.book_comment.BookCommentService;
import ru.otus.filinovich.util.MessageProvider;

@ShellComponent
@RequiredArgsConstructor
public class BookCommentShell {

    private final BookCommentService bookCommentService;

    private final MessageProvider messageProvider;

    @ShellMethod(value = "Show comments of book", key = "comments")
    public String showCommentsOfBook(@ShellOption String bookId) {
        return bookCommentService.getAllCommentsDescriptionWithIdByBookId(bookId);
    }

    @ShellMethod(value = "Show comment by ID", key = "comment")
    public String showCommentById(@ShellOption String id) {
        return bookCommentService.getCommentDescriptionById(id);
    }

    @ShellMethod(value = "Create comment of book", key = "create-comment")
    public String createComment() {
        BookComment newComment = bookCommentService.createComment();
        return bookCommentService.getCommentDescription(newComment);
    }

    @ShellMethod(value = "Update comment of book", key = "update-comment")
    public String updateComment() {
        return bookCommentService.updateCommentAndGetDescription();
    }

    @ShellMethod(value = "Delete comment of book", key = "delete-comment")
    public String deleteComment(@ShellOption String commentId) {
        if (bookCommentService.deleteById(commentId)) {
            return messageProvider.getMessage("comments.success_delete");
        } else {
            return messageProvider.getMessage("comments.failed_delete");
        }
    }
}
