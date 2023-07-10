package ru.job4j.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "full_name")
    private String fullName;
    private String email;
    private String password;
    @Column(columnDefinition = "creation_date")
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    private LocalDateTime creationDateTime = LocalDateTime.now();
}
