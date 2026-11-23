
package edu.swe307.web.employee;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository {

    private static final String FIND_BY_NAME_SQL =
            "SELECT e.empno, e.ename, e.sal, e.comm, " +
            "e.img, d.dname, m.ename AS mgr_name " +
            "FROM emp e " +
            "JOIN dept d ON d.deptno = e.deptno " +
            "LEFT JOIN emp m ON m.empno = e.mgr " +
            "WHERE UPPER(e.ename) = UPPER(?)";

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Employee> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }

        List<Employee> employees = jdbcTemplate.query(
                FIND_BY_NAME_SQL,
                (rs, rowNum) -> new Employee(
                        rs.getInt("empno"),
                        rs.getString("ename"),
                        rs.getString("mgr_name"),
                        rs.getBigDecimal("sal"),
                        rs.getBigDecimal("comm"),
                        rs.getString("dname"),
                        rs.getString("img")
                ),
                name.trim()
        );

        return employees.stream().findFirst();
    }
}
