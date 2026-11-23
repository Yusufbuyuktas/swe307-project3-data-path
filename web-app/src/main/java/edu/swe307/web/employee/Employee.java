
package edu.swe307.web.employee;

import java.math.BigDecimal;

public class Employee {

    private final int empno;
    private final String ename;
    private final String mgrName;
    private final BigDecimal salary;
    private final BigDecimal commission;
    private final String department;
    private final String imageFile;

    public Employee(
            int empno,
            String ename,
            String mgrName,
            BigDecimal salary,
            BigDecimal commission,
            String department,
            String imageFile) {

        this.empno = empno;
        this.ename = ename;
        this.mgrName = mgrName;
        this.salary = salary;
        this.commission = commission;
        this.department = department;
        this.imageFile = imageFile;
    }

    public int getEmpno() {
        return empno;
    }

    public String getEname() {
        return ename;
    }

    public String getMgrName() {
        return mgrName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public String getDepartment() {
        return department;
    }

    public String getImageFile() {
        return imageFile;
    }
}
