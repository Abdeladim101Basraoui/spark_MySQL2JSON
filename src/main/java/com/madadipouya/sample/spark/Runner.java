package com.madadipouya.sample.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.sql.SQLException;

public class Runner {

    private static final int TEST_DATA_SIZE = 3000000;

    private DbInitializer dbInitializer;

    private PropertyLoader propertyLoader;

    public Runner() {
        this.propertyLoader = new PropertyLoader();
        this.dbInitializer = new DbInitializer(propertyLoader);
    }

    public static void main(String[] args) throws SQLException {
        Runner runner = new Runner();
        runner.insertTestData();
        runner.transformToJson();
    }

    private void insertTestData() throws SQLException {
        dbInitializer.insertTestData(TEST_DATA_SIZE);
    }

    private void transformToJson() {
        SparkSession spark = SparkSession.builder()
                .config("spark.master", "local")
                .appName("Java Spark SQL basic example").getOrCreate();
        String query = "SELECT id, title, description, author, year, edition, publisher FROM books order by id";
        Dataset<Row> jdbcDF = spark.read().format("jdbc")
                .option("url", propertyLoader.getJdbcConnectionString())
                .option("user", propertyLoader.getDatabaseUserName())
                .option("password", propertyLoader.getDatabasePassword())
                .option("numPartitions", "10")
                .option("partitionColumn", "id")
                .option("lowerBound", "0")
                .option("upperBound", Long.MAX_VALUE)
                // Use to partition by date
                /*.option("partitionColumn", "inserted")
                .option("lowerBound", "1930-01-01 00:00:00")
                .option("upperBound", "2030-01-01 00:00:00")*/
                .option("driver", "com.mysql.jdbc.Driver")
                .option("dbtable", String.format("(%s) AS tmp", query))
                .load();
        jdbcDF.write().format("json").mode("append").save("books_json");
        spark.close();
    }
}