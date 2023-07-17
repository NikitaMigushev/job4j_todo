CREATE TABLE task_category (
  id serial PRIMARY KEY,
  task_id INT NOT NULL REFERENCES tasks (id),
  category_id INT NOT NULL REFERENCES category (id),
  UNIQUE (task_id, category_id)
);