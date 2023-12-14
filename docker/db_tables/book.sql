CREATE TABLE IF NOT EXISTS books (
  id INTEGER AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(512) NOT NULL,
  description VARCHAR(2048) NOT NULL,
  author VARCHAR(256) NOT NULL,
  year INTEGER NOT NULL,
  edition INTEGER NOT NULL,
  publisher VARCHAR(512) NOT NULL,
  inserted DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) engine=InnoDB;