# job4j_todo

## Описание проекта

Простое приложение для управления задачами. Данное приложение демонстрирует использование стека технологий Spring boot, Thymeleaf, Bootstrap, Hibernate, PostgreSql.
Список задач может быть полезен для того, чтобы создавать задачи, видеть список задач, менять и отслеживать статус задач.

## Стек технологий
- Java 17
- PostgreSQL 15.2
- springframework.boot 2.7.6
- Hibernate

## Требования к окружению
- Java 17
- PostgreSQL 15.2
- apache-maven-3.9.1

## Запуск проекта

- создать базу данных 'todo' в PostgreSql
- проверить настройки подключения к базе данных в файлах:
    - db/liquibase.properties
    - src/main/resources/hibernate.cfg.xml
- перед запуском запустить команду liquibase:update
- запустить метод main
- открыть в браузере http://localhost:8080/

## Взаимодействие с приложением
### Авторизация
<image src="https://github.com/NikitaMigushev/job4j_todo/blob/master/img/1.png" alt="Авторизация" />

### Регистрация
<image src="https://github.com/NikitaMigushev/job4j_todo/blob/master/img/2.png" alt="Регистрация" />

### Все задачи
<image src="https://github.com/NikitaMigushev/job4j_todo/blob/master/img/3.png" alt="Все задачи" />

### Фильтрация задачи
<image src="https://github.com/NikitaMigushev/job4j_todo/blob/master/img/4.png" alt="Фильтрация задачи" />

### Добавление новой задачи
<image src="https://github.com/NikitaMigushev/job4j_todo/blob/master/img/5.png" alt="Добавление новой задачи" />

### Редактирование задачи
<image src="https://github.com/NikitaMigushev/job4j_todo/blob/master/img/6.png" alt="Редактирование задачи" />

