package ru.otus.filinovich.authenticationservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {
  @Id
  private String id;

  private String username;

  private String email;

  private String password;

  private boolean banned;

  @DBRef
  private Set<Role> roles = new HashSet<>();

  private Set<Book> books = new HashSet<>();

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
