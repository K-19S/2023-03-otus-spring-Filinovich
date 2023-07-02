package ru.otus.filinovich.dao.book_comments;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.filinovich.domain.BookComment;

import java.util.List;

public interface BookCommentRepository extends MongoRepository<BookComment, String> {

    List<BookComment> findByBookId(String id);
}
