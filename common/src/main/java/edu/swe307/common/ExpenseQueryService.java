package edu.swe307.common;

import java.math.BigDecimal;

/**
 * Contract for querying total employee expenses from Cassandra via Spark (Owner: A2).
 */
public interface ExpenseQueryService {

    /**
     * Calculates the total expense amount for a given employee number.
     *
     * @param empno employee ID (e.g., 7788 for SCOTT)
     * @return total expense amount, or {@link BigDecimal#ZERO} if the employee has no expenses
     */
    BigDecimal totalExpense(int empno);
}
