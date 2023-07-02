package ru.otus.filinovich.dao.genre;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.filinovich.domain.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {

}
