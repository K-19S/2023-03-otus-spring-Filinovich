package ru.otus.filinovich.dao.author;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.filinovich.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {

}
