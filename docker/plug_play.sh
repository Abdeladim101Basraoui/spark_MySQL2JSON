#!/bin/bash

# Docker MySQL container settings
MYSQL_CONTAINER_NAME="mysql-library"
MYSQL_ROOT_PASSWORD="secret"
MYSQL_DATABASE="library"

# SQL script file
SQL_SCRIPT="generate_books.sql"

# Create the SQL script file
echo "USE $MYSQL_DATABASE;

-- Create the books table if it doesn't exist
CREATE TABLE IF NOT EXISTS books (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(512) NOT NULL,
    description VARCHAR(2048) NOT NULL,
    author VARCHAR(256) NOT NULL,
    year INT NOT NULL,
    edition INT NOT NULL,
    publisher VARCHAR(512) NOT NULL,
    inserted DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Generate 1 million records
DELIMITER $$
CREATE PROCEDURE GenerateBooks()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 355282 DO
        INSERT INTO books (title, description, author, year, edition, publisher)
        VALUES (
            CONCAT('Title ', i),
            CONCAT('Description for book ', i),
            CONCAT('Author ', i),
            YEAR(NOW()) - RAND() * 50, -- Random year between 50 years ago and the current year
            CEIL(RAND() * 10), -- Random edition between 1 and 10
            CONCAT('Publisher ', i)
        );
        SET i = i + 1;
    END WHILE;
END $$
DELIMITER ;

-- Call the stored procedure to generate records
CALL GenerateBooks();" > "$SQL_SCRIPT"

# Run the Docker container with MySQL
# docker run --name "$MYSQL_CONTAINER_NAME" -e MYSQL_ROOT_PASSWORD="$MYSQL_ROOT_PASSWORD" -d -p 3306:3306 mysql

# Wait for MySQL to start (adjust the sleep time based on your MySQL container startup time)
echo "Waiting for MySQL to start..."
sleep 30

# Execute the SQL script in the Docker container
docker exec -i "$MYSQL_CONTAINER_NAME" mysql -uroot -p"$MYSQL_ROOT_PASSWORD" "$MYSQL_DATABASE" < "$SQL_SCRIPT"

# Remove the temporary SQL script
rm "$SQL_SCRIPT"

echo "Database populated with 1 million records!"
