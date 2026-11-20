package edu.swe307.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Data Transfer Object representing the 7-column employee view model (Owner: A3).
 */
public class EmployeeRow implements Serializable {

    private static final long serialVersionUID = 1L;

    private String imageFile;
    private String ename;
    private String mgrName;       // Nullable (e.g., KING has no manager)
    private BigDecimal salary;
    private BigDecimal commission; // Nullable (null or 0 for non-sales roles)
    private String department;
    private BigDecimal totalExpense;

    public EmployeeRow() {
    }

    public EmployeeRow(
            String imageFile,
            String ename,
            String mgrName,
            BigDecimal salary,
            BigDecimal commission,
            String department,
            BigDecimal totalExpense) {
        this.imageFile = imageFile;
        this.ename = ename;
        this.mgrName = mgrName;
        this.salary = salary;
        this.commission = commission;
        this.department = department;
        this.totalExpense = totalExpense;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getMgrName() {
        return mgrName;
    }

    public void setMgrName(String mgrName) {
        this.mgrName = mgrName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeRow)) return false;
        EmployeeRow that = (EmployeeRow) o;
        return Objects.equals(imageFile, that.imageFile)
                && Objects.equals(ename, that.ename)
                && Objects.equals(mgrName, that.mgrName)
                && Objects.equals(salary, that.salary)
                && Objects.equals(commission, that.commission)
                && Objects.equals(department, that.department)
                && Objects.equals(totalExpense, that.totalExpense);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageFile, ename, mgrName, salary, commission, department, totalExpense);
    }

    @Override
    public String toString() {
        return "EmployeeRow{" +
                "imageFile='" + imageFile + '\'' +
                ", ename='" + ename + '\'' +
                ", mgrName='" + mgrName + '\'' +
                ", salary=" + salary +
                ", commission=" + commission +
                ", department='" + department + '\'' +
                ", totalExpense=" + totalExpense +
                '}';
    }
}
