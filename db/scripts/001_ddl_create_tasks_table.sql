CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   name VARCHAR(255),
   description TEXT,
   created TIMESTAMP,
   deadline TIMESTAMP,
   done BOOLEAN
);