package edu.swe307.common;

import java.util.List;

/**
 * Shared system constants across streaming, query, and web modules (Owner: A3).
 */
public final class ProjectConstants {

    private ProjectConstants() {
    }

    // Kafka Topics (Department names)
    public static final String KAFKA_ACCOUNTING = "accounting";
    public static final String KAFKA_RESEARCH = "research";
    public static final String KAFKA_SALES = "sales";
    public static final String KAFKA_OPERATIONS = "operations";

    public static final List<String> KAFKA_TOPICS = List.of(
            KAFKA_ACCOUNTING,
            KAFKA_RESEARCH,
            KAFKA_SALES,
            KAFKA_OPERATIONS
    );

    // Cassandra Keyspace & Table
    public static final String CASSANDRA_KEYSPACE = "pos";
    public static final String CASSANDRA_TABLE = "expenses";

    // Cassandra Columns (Draft schema — subject to Task 6.3 POS simulator verification)
    public static final String COL_USER_ID = "user_id";
    public static final String COL_DATE_TIME = "date_time";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_AMOUNT = "amount";

    // HDFS Storage
    public static final String HDFS_PHOTOS_DIR = "/photos";
}
