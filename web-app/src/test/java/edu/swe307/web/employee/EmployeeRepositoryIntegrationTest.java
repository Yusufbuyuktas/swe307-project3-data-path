
package edu.swe307.web.employee;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class EmployeeRepositoryIntegrationTest {

    private EmployeeRepository createRepository() {
        String password = System.getenv("SWE307_MYSQL_PASSWORD");

        assumeTrue(password != null && !password.isEmpty(),
                "MySQL integration test credentials not provided");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(
                "jdbc:mysql://127.0.0.1:3306/bigdata"
                + "?useSSL=false&allowPublicKeyRetrieval=true"
                + "&serverTimezone=UTC"
        );
        dataSource.setUsername("bduser");
        dataSource.setPassword(password);

        return new EmployeeRepository(new JdbcTemplate(dataSource));
    }

    @Test
    void findsScottIgnoringCaseAndWhitespace() {
        Employee employee = createRepository()
                .findByName(" scott ")
                .orElseThrow();

        assertEquals(7788, employee.getEmpno());
        assertEquals("SCOTT", employee.getEname());
        assertEquals("JONES", employee.getMgrName());
        assertEquals("RESEARCH", employee.getDepartment());
        assertEquals("scott.jpg", employee.getImageFile());
        assertEquals(0, new BigDecimal("3000.00")
                .compareTo(employee.getSalary()));
    }

    @Test
    void kingHasNoManager() {
        Employee king = createRepository()
                .findByName("KING")
                .orElseThrow();

        assertEquals(7839, king.getEmpno());
        assertNull(king.getMgrName());
        assertNull(king.getCommission());
    }

    @Test
    void turnerCommissionIsZero() {
        Employee turner = createRepository()
                .findByName("TURNER")
                .orElseThrow();

        assertNotNull(turner.getCommission());
        assertEquals(0, BigDecimal.ZERO
                .compareTo(turner.getCommission()));
    }

    @Test
    void unknownEmployeeReturnsEmpty() {
        assertTrue(createRepository()
                .findByName("NONEXISTENT")
                .isEmpty());
    }
}
