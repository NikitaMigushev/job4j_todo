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
    - src/main/resources/application.properties
    - src/test/resources/connection.properties
- перед запуском запустить команду liquibase:update
- запустить метод main
- открыть в браузере http://localhost:8080/

## Взаимодействие с приложением
Авторизация

Все задачи

Фильтрация задачи

Добавление новой задачи

Редактирование задачи

