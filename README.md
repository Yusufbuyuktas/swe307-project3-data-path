# SWE307 Project 3 — Kafka Spark Cassandra Data Path

## 1. Project Overview

Real-time and batch data pipeline processing Point-of-Sale (POS) employee expense streams through Apache Kafka, Apache Spark Structured Streaming, and Apache Cassandra, combined with MySQL master data and HDFS employee portraits in a Spring Boot web interface.

## 2. Architecture

- **Streaming Path:** POS producer (`scripts/pos_producer.py`) → Kafka (`accounting`, `research`, `sales`, `operations`) → PySpark Structured Streaming (`scripts/stream_expenses.py`) → Cassandra (`pos.expenses`)
- **Serving Path:** Web UI (`web-app`) → MySQL (`bigdata.emp`, `bigdata.dept`) + HDFS (`/photos/{filename}`) + Spark Query (`spark-query` → Cassandra)
- **Dependency Rules:**
  - `web-app` → `spark-query` → `common`
  - `streaming-job` → `common`
  - `common` remains pure Java (no Spring Boot or Spark dependencies).
  - `web-app` does not depend on `streaming-job`.

## 3. Technology Stack

- **Language & Build:** Java 11, Python 3.10, Apache Maven 3.8+
- **Streaming & Batch Processing:** Apache Spark 3.3.3 (Scala 2.12), Spark Cassandra Connector 3.3.0
- **Messaging:** Apache Kafka
- **Storage:** Apache Cassandra (`pos.expenses`), MySQL (`8.0.46-0ubuntu0.22.04.5`, `bigdata`), Apache HDFS (`/photos`)
- **Web Framework:** Spring Boot 2.7.18, Spring MVC, Thymeleaf

## 4. Requirements

- Ubuntu / WSL2 environment (project located under Ubuntu home directory `~/swe307/`)
- JDK 11, Maven 3.8+, and Python 3.10 for the A1 scripts
- Running local instances of Kafka, Cassandra, MySQL, and HDFS

## 5. Module Structure

- `common` (`edu.swe307.common`): Shared constants, DTOs (`EmployeeRow`), and service interfaces (`ExpenseQueryService`, `PhotoService`) — **Owner: A3**
- `streaming-job` (`edu.swe307.streaming`): Java streaming prototype and CSV parser tests — **Owner: A1**
- `scripts/`: POS producer and PySpark streaming job — **Owner: A1**
- `spark-query` (`edu.swe307.query`): Spark-based total expense calculation (`totalExpense`) — **Owner: A2**
- `web-app` (`edu.swe307.web`): Spring Boot MVC controller, Thymeleaf UI, MySQL & HDFS integration — **Owners: B1 + B2**

## 6. Build Instructions

Run from the repository root:

```bash
mvn clean verify
```

## 7. Running the System

- **MySQL Schema & Seed (`bigdata`):** `TODO — owner: B1`
- **Cassandra Schema (`infra/cassandra/init.cql`):** `TODO — owner: A2`
- **HDFS Photo Upload (`/photos`):** `TODO — owner: B2`
- **Spark Query Integration (`spark-query`):** `TODO — owner: A2`
- **Spring Boot Web App (`web-app`):** `TODO — owner: B1 & B2`

### A1 · POS Producer and PySpark Streaming

The producer selects 10 random expense records from `expense.csv` and uses
`emp.csv` and `dept.csv` to route each record to the employee's department topic.
It sends one record approximately every second. PySpark consumes all four topics
and writes the fields `user_id`, `date_time`, `description`, `type`, `count`, and
`payment` to `pos.expenses`. The producer preserves `payment` as the transaction total.

#### Prepare the environment

Run the commands below from the repository root. Make sure Kafka is listening on
`127.0.0.1:9092`, the four department topics exist, and Cassandra is listening on
`127.0.0.1:9042` with the six-column `pos.expenses` schema.

Place `expense.csv`, `emp.csv`, and `dept.csv` from the
[course repository](https://github.com/ozmen54/SWE307-2026) in `data/samples/`.
On Ubuntu 22.04, install `python3.10-venv` if needed, then create the producer environment:

```bash
python3.10 -m venv "$HOME/swe307/pos-venv" &&
"$HOME/swe307/pos-venv/bin/python" -m pip install 'kafka-python==2.2.15'
```

#### Run the stream

In the first terminal, start PySpark and leave it running:

```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export PYSPARK_PYTHON=/usr/bin/python3.10

"$HOME/tools/spark-3.3.3-bin-hadoop3/bin/spark-submit" \
  --master 'local[2]' \
  --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.3.3,com.datastax.spark:spark-cassandra-connector_2.12:3.3.0 \
  scripts/stream_expenses.py
```

In a second terminal, start the producer:

```bash
"$HOME/swe307/pos-venv/bin/python" scripts/pos_producer.py
```

Keep `~/p3-data/checkpoint-pyspark` when restarting the stream so it can resume
from its saved Kafka offsets.

#### Verification

| Test | Cassandra records found | Sales | Research | Accounting | Operations |
| --- | ---: | ---: | ---: | ---: | ---: |
| Live run with PySpark active | 10/10 | 7/7 | 1/1 | 2/2 | 0 sent |
| Restart with the same checkpoint after publishing while PySpark was stopped | 10/10 | 8/8 | 1/1 | 1/1 | 0 sent |

In the live run, the producer timestamps were approximately one second apart.
Records were verified by querying Cassandra. The source employee CSV has no
Operations employees.

## 8. Team Responsibilities

| **Member** | **Scope** |
| --- | --- |
| **A1** | Kafka topics, POS simulator integration, PySpark streaming to Cassandra |
| **A2** | Cassandra schema (`pos.expenses`), `spark-query` (`ExpenseQueryService`) |
| **B1** | MySQL (`bigdata`), employee search & 7-column Thymeleaf UI |
| **B2** | HDFS `/photos` (`PhotoService`), Spring Boot + Spark dependency alignment |
| **A3** | Architecture baseline, root POM, `common` contracts, `docs/02-sozlesmeler.md`, E2E QA |

## 9. Git Workflow

- `main` always holds compilable, stable code.
- Create task branches from `main` using `feat/<topic>` or `fix/<topic>`.
- Merge only via Pull Request with at least 1 code review. Do not push directly to `main`.

## 10. Testing

- Run unit and contract tests with `mvn clean verify`.
- Acceptance criteria and edge cases (`KING` null manager, `TURNER` 0 commission, 0 expense employee, restart idempotency) are documented in `docs/02-sozlesmeler.md`.
