CREATE TABLE users (
  username VARCHAR(100) PRIMARY KEY,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE tasks (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  owner VARCHAR(100),
  title VARCHAR(255),
  description TEXT,
  due_date DATE,
  completed BOOLEAN,
  FOREIGN KEY (owner) REFERENCES users(username)
);