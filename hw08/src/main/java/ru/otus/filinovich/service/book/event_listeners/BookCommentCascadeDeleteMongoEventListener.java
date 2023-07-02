package ru.otus.filinovich.service.book.event_listeners;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.filinovich.dao.book_comments.BookCommentRepository;
import ru.otus.filinovich.domain.Book;
import ru.otus.filinovich.domain.BookComment;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookCommentCascadeDeleteMongoEventListener extends AbstractMongoEventListener<Book> {

    private final BookCommentRepository bookCommentRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        Document source = event.getSource();
        ObjectId bookId = source.getObjectId("_id");
        List<BookComment> comments = bookCommentRepository.findByBookId(bookId.toString());
        bookCommentRepository.deleteAll(comments);
    }
}
