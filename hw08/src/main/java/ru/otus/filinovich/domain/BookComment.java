package ru.otus.filinovich.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookComments")
public class BookComment {

    private String id;

    private String text;

    @DBRef
    private Book book;

    public BookComment(String text, Book book) {
        this.text = text;
        this.book = book;
    }
}
