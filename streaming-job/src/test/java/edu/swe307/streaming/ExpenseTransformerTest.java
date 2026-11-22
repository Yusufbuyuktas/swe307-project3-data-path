package edu.swe307.streaming;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpenseTransformerTest {

    @Test
    void mapsAllSixFields() {
        ExpenseTransformer.Expense expense = ExpenseTransformer
            .fromCsv("7788,2026-11-22 14:30:00,coffee,Food,2,20.50")
            .orElseThrow();

        assertEquals(7788, expense.getUserId());
        assertEquals("2026-11-22 14:30:00", expense.getDateTime());
        assertEquals("coffee", expense.getDescription());
        assertEquals("Food", expense.getType());
        assertEquals(2, expense.getCount());
        assertEquals(new BigDecimal("20.50"), expense.getPayment());
    }

    @Test
    void readsQuotedDescriptions() {
        ExpenseTransformer.Expense expense = ExpenseTransformer
            .fromCsv("7788,2026-11-22 14:30:00,\"coffee, \"\"large\"\"\",Food,2,20.50")
            .orElseThrow();

        assertEquals("coffee, \"large\"", expense.getDescription());
    }

    @Test
    void rejectsMalformedRows() {
        assertTrue(ExpenseTransformer.fromCsv(null).isEmpty());
        assertTrue(ExpenseTransformer.fromCsv("7788,time,coffee,20.50").isEmpty());
        assertTrue(ExpenseTransformer.fromCsv("7788,time,coffee,Food,x,20.50").isEmpty());
        assertTrue(ExpenseTransformer.fromCsv("7788,time,coffee,Food,2,x").isEmpty());
    }
}
