package edu.swe307.common;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonContractTest {

    @Test
    void shouldDefineExpectedKafkaAndCassandraConstants() {
        assertEquals("accounting", ProjectConstants.KAFKA_ACCOUNTING);
        assertEquals("research", ProjectConstants.KAFKA_RESEARCH);
        assertEquals("sales", ProjectConstants.KAFKA_SALES);
        assertEquals("operations", ProjectConstants.KAFKA_OPERATIONS);
        assertEquals(List.of("accounting", "research", "sales", "operations"), ProjectConstants.KAFKA_TOPICS);

        assertEquals("pos", ProjectConstants.CASSANDRA_KEYSPACE);
        assertEquals("expenses", ProjectConstants.CASSANDRA_TABLE);
        assertEquals("/photos", ProjectConstants.HDFS_PHOTOS_DIR);
    }

    @Test
    void shouldCreateEmployeeRowWithNullableFieldsAndBigDecimalAmounts() {
        EmployeeRow king = new EmployeeRow(
                "king.jpg",
                "KING",
                null,
                new BigDecimal("5000.00"),
                null,
                "ACCOUNTING",
                BigDecimal.ZERO
        );

        assertEquals("king.jpg", king.getImageFile());
        assertEquals("KING", king.getEname());
        assertNull(king.getMgrName());
        assertEquals(new BigDecimal("5000.00"), king.getSalary());
        assertNull(king.getCommission());
        assertEquals("ACCOUNTING", king.getDepartment());
        assertEquals(BigDecimal.ZERO, king.getTotalExpense());
    }

    @Test
    void shouldEnforceExpectedFieldTypesAndServiceSignatures() throws NoSuchFieldException, NoSuchMethodException {
        assertEquals(7, EmployeeRow.class.getDeclaredFields().length - 1); // excluding serialVersionUID

        Field salaryField = EmployeeRow.class.getDeclaredField("salary");
        Field commissionField = EmployeeRow.class.getDeclaredField("commission");
        Field totalExpenseField = EmployeeRow.class.getDeclaredField("totalExpense");

        assertEquals(BigDecimal.class, salaryField.getType());
        assertEquals(BigDecimal.class, commissionField.getType());
        assertEquals(BigDecimal.class, totalExpenseField.getType());

        Method totalExpenseMethod = ExpenseQueryService.class.getMethod("totalExpense", int.class);
        assertEquals(BigDecimal.class, totalExpenseMethod.getReturnType());

        Method getPhotoMethod = PhotoService.class.getMethod("getPhoto", String.class);
        assertEquals(byte[].class, getPhotoMethod.getReturnType());
    }
}
