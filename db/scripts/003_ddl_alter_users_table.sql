ALTER TABLE TASKS
ADD COLUMN todo_user int not null references users(id);