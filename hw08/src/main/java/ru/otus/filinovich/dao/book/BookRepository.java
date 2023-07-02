package ru.otus.filinovich.dao.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.filinovich.domain.Book;

public interface BookRepository extends MongoRepository<Book, String> {

}
