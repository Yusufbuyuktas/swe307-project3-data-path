# SWE307 Project 3 — Kafka Spark Cassandra Data Path

## 1. Project Overview
Real-time and batch data pipeline processing Point-of-Sale (POS) employee expense streams through Apache Kafka, Apache Spark Structured Streaming, and Apache Cassandra, combined with MySQL master data and HDFS employee portraits in a Spring Boot web interface.

## 2. Architecture
* **Streaming Path:** POS Simulator → Kafka (`accounting`, `research`, `sales`, `operations`) → Spark Structured Streaming (`streaming-job`) → Cassandra (`pos.expenses`)
* **Serving Path:** Web UI (`web-app`) → MySQL (`bigdata.emp`, `bigdata.dept`) + HDFS (`/photos/{filename}`) + Spark Query (`spark-query` → Cassandra)
* **Dependency Rules:**
  * `web-app` → `spark-query` → `common`
  * `streaming-job` → `common`
  * `common` remains pure Java (no Spring Boot or Spark dependencies).
  * `web-app` does not depend on `streaming-job`.

## 3. Technology Stack
* **Language & Build:** Java 11, Apache Maven 3.8+
* **Streaming & Batch Processing:** Apache Spark 3.3.3 (Scala 2.12), Spark Cassandra Connector 3.3.0
* **Messaging:** Apache Kafka
* **Storage:** Apache Cassandra (`pos.expenses`), MySQL (`8.0.46-0ubuntu0.22.04.5`, `bigdata`), Apache HDFS (`/photos`)
* **Web Framework:** Spring Boot 2.7.18, Spring MVC, Thymeleaf

## 4. Requirements
* Ubuntu / WSL2 environment (project located under Ubuntu home directory `~/swe307/`)
* JDK 11, Maven 3.8+
* Running local instances of Kafka, Cassandra, MySQL, and HDFS

## 5. Module Structure
* `common` (`edu.swe307.common`): Shared constants, DTOs (`EmployeeRow`), and service interfaces (`ExpenseQueryService`, `PhotoService`) — **Owner: A3**
* `streaming-job` (`edu.swe307.streaming`): Kafka consumer & Spark Structured Streaming writer to Cassandra — **Owner: A1**
* `spark-query` (`edu.swe307.query`): Spark-based total expense calculation (`totalExpense`) — **Owner: A2**
* `web-app` (`edu.swe307.web`): Spring Boot MVC controller, Thymeleaf UI, MySQL & HDFS integration — **Owners: B1 + B2**

## 6. Build Instructions
Run from the repository root:
```bash
mvn clean verify
```

## 7. Running the System
* **MySQL Schema & Seed (`bigdata`):** `TODO — owner: B1`
* **Cassandra Schema (`infra/cassandra/init.cql`):** `TODO — owner: A2`
* **HDFS Photo Upload (`/photos`):** `TODO — owner: B2`
* **Kafka Topics & Streaming Job (`streaming-job`):** `TODO — owner: A1`
* **Spark Query Integration (`spark-query`):** `TODO — owner: A2`
* **Spring Boot Web App (`web-app`):** `TODO — owner: B1 & B2`

## 8. Team Responsibilities
| Member | Scope |
| :--- | :--- |
| **A1** | Kafka topics, POS simulator integration, `streaming-job` |
| **A2** | Cassandra schema (`pos.expenses`), `spark-query` (`ExpenseQueryService`) |
| **B1** | MySQL (`bigdata`), employee search & 7-column Thymeleaf UI |
| **B2** | HDFS `/photos` (`PhotoService`), Spring Boot + Spark dependency alignment |
| **A3** | Architecture baseline, root POM, `common` contracts, `docs/02-sozlesmeler.md`, E2E QA |

## 9. Git Workflow
* `main` always holds compilable, stable code.
* Create task branches from `main` using `feat/<topic>` or `fix/<topic>`.
* Merge only via Pull Request with at least 1 code review; never push directly to `main`.

## 10. Testing
* Run unit and contract tests with `mvn clean verify`.
* Acceptance criteria and edge cases (`KING` null manager, `TURNER` 0 commission, 0 expense employee, restart idempotency) are documented in `docs/02-sozlesmeler.md`.
