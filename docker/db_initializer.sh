#!/bin/bash

docker exec -i mysql-library mysql -u root --password=secret library < db_tables/book.sql