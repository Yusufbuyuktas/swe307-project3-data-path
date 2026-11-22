package edu.swe307.common;

import java.util.List;

public final class Topics {
    private Topics() {}

    public static final String ACCOUNTING = ProjectConstants.KAFKA_ACCOUNTING;
    public static final String RESEARCH = ProjectConstants.KAFKA_RESEARCH;
    public static final String SALES = ProjectConstants.KAFKA_SALES;
    public static final String OPERATIONS = ProjectConstants.KAFKA_OPERATIONS;
    public static final List<String> ALL = ProjectConstants.KAFKA_TOPICS;
}
