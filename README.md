# Sample Spark Java

![](https://i0.wp.com/www.geekyhacker.com/wp-content/uploads/2020/04/Workflow.jpg?w=1000&ssl=1)

A simple working example of Spark with Java.

## How to run

### Bring up docker and create table

```bash
$ docker-compose up -d
$ cd docker
$ ./plug_play.sh
```

### Compile and run with Maven



First 1 million records test rows inserted to the database. Then Sparks read those records and convert them to JSON 
in `books_json` directory. 
