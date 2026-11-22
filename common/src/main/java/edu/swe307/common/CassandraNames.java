package edu.swe307.common;

public final class CassandraNames {
    private CassandraNames() {}

    public static final String KEYSPACE = ProjectConstants.CASSANDRA_KEYSPACE;
    public static final String TABLE_EXPENSES = ProjectConstants.CASSANDRA_TABLE;
    public static final String COL_USER_ID = ProjectConstants.COL_USER_ID;
    public static final String COL_DATE_TIME = ProjectConstants.COL_DATE_TIME;
    public static final String COL_DESCRIPTION = ProjectConstants.COL_DESCRIPTION;
    public static final String COL_AMOUNT = ProjectConstants.COL_AMOUNT;
}
