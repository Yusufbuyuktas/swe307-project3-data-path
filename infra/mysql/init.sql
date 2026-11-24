
-- SWE307 Project 3 - MySQL initialization
-- Owner: B1
-- Source: data/samples/dept.csv, data/samples/emp.csv

CREATE DATABASE IF NOT EXISTS bigdata
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE bigdata;

-- Departments
CREATE TABLE IF NOT EXISTS dept (
    deptno INT PRIMARY KEY,
    dname VARCHAR(50) NOT NULL,
    loc VARCHAR(50) NOT NULL
);

-- Employees
CREATE TABLE IF NOT EXISTS emp (
    empno INT PRIMARY KEY,
    ename VARCHAR(50) NOT NULL,
    job VARCHAR(50) NOT NULL,
    mgr INT NULL,
    hiredate DATE NOT NULL,
    sal DECIMAL(10,2) NOT NULL,
    comm DECIMAL(10,2) NULL,
    deptno INT NOT NULL,
    img VARCHAR(255) NOT NULL,

    CONSTRAINT fk_emp_dept
        FOREIGN KEY (deptno) REFERENCES dept(deptno)
);

CREATE INDEX idx_emp_ename ON emp(ename);

-- Department seed data
INSERT INTO dept (deptno, dname, loc) VALUES
(10, 'ACCOUNTING', 'NEW YORK'),
(20, 'RESEARCH', 'DALLAS'),
(30, 'SALES', 'CHICAGO'),
(40, 'OPERATIONS', 'BOSTON');

-- Employee seed data
-- Dates converted from CSV format to ISO YYYY-MM-DD.
-- Empty mgr/comm values are stored as NULL.

INSERT INTO emp
(empno, ename, job, mgr, hiredate, sal, comm, deptno, img)
VALUES
(7369, 'SMITH', 'CLERK', 7902, '1980-12-17', 800, NULL, 20, 'smith.jpg'),
(7499, 'ALLEN', 'SALESMAN', 7698, '1981-02-20', 1600, 300, 30, 'allen.jpg'),
(7521, 'WARD', 'SALESMAN', 7698, '1981-02-22', 1250, 500, 30, 'ward.jpg'),
(7566, 'JONES', 'MANAGER', 7839, '1981-04-02', 2975, NULL, 20, 'jones.jpg'),
(7654, 'MARTIN', 'SALESMAN', 7698, '1981-09-28', 1250, 1400, 30, 'martin.jpg'),
(7698, 'BLAKE', 'MANAGER', 7839, '1981-05-01', 2850, NULL, 30, 'blake.jpg'),
(7782, 'CLARK', 'MANAGER', 7839, '1981-06-09', 2450, NULL, 10, 'clark.jpg'),
(7788, 'SCOTT', 'ANALYST', 7566, '1982-12-09', 3000, NULL, 20, 'scott.jpg'),
(7839, 'KING', 'PRESIDENT', NULL, '1981-11-17', 5000, NULL, 10, 'king.jpg'),
(7844, 'TURNER', 'SALESMAN', 7698, '1981-09-08', 1500, 0, 30, 'turner.jpg'),
(7876, 'ADAMS', 'CLERK', 7788, '1983-01-12', 1100, NULL, 20, 'adams.jpg'),
(7900, 'JAMES', 'CLERK', 7698, '1981-12-03', 950, NULL, 30, 'james.jpg'),
(7902, 'FORD', 'ANALYST', 7566, '1981-12-03', 3000, NULL, 20, 'ford.jpg'),
(7934, 'MILLER', 'CLERK', 7782, '1982-01-23', 1300, NULL, 10, 'miller.jpg');

-- Local development account
CREATE USER IF NOT EXISTS 'bduser'@'localhost'
    IDENTIFIED BY 'bdpass123';

GRANT SELECT ON bigdata.* TO 'bduser'@'localhost';
