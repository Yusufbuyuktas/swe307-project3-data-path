package edu.swe307.query;

import java.math.BigDecimal;

public class QueryDemo {

    public static void main(String[] args) {
        SparkExpenseQueryService service = new SparkExpenseQueryService();

        try {
            for (int i = 1; i <= 6; i++) {
                long start = System.currentTimeMillis();
                BigDecimal total = service.totalExpense(7788);
                long elapsed = System.currentTimeMillis() - start;
                System.out.printf("Cagri %d | user_id=7788 | total=%s | sure=%d ms%n", i, total, elapsed);
            }

            long start = System.currentTimeMillis();
            BigDecimal emptyTotal = service.totalExpense(9999);
            long elapsed = System.currentTimeMillis() - start;
            System.out.printf("Kayitsiz | user_id=9999 | total=%s | sure=%d ms%n", emptyTotal, elapsed);
        } finally {
            SparkSessionFactory.stop();
        }
    }
}
