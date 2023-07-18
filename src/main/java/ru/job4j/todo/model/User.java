package ru.job4j.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @Column(name = "full_name")
    private String fullName;
    @EqualsAndHashCode.Include
    private String email;
    private String password;
    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();
    private String timezone = TimeZone.getDefault().getID();
}
