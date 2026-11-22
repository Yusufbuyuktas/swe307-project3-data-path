import csv
import io
import random
import time
from datetime import datetime
from pathlib import Path

from kafka import KafkaProducer

DATA_DIR = Path(__file__).resolve().parents[1] / "data" / "samples"


def read_csv(filename):
    with (DATA_DIR / filename).open(encoding="utf-8-sig", newline="") as file:
        reader = csv.DictReader(file)
        reader.fieldnames = [name.strip().lower() for name in reader.fieldnames]
        return list(reader)


def main():
    departments = {
        row["deptno"].strip(): row["dname"].strip().lower()
        for row in read_csv("dept.csv")
    }
    employee_topics = {
        row["empno"].strip(): departments[row["deptno"].strip()]
        for row in read_csv("emp.csv")
    }

    samples = random.sample(read_csv("expense.csv"), 10)
    producer = KafkaProducer(bootstrap_servers="127.0.0.1:9092", acks="all")

    try:
        for index, expense in enumerate(samples):
            if index:
                time.sleep(1)

            employee_id = expense["empno"].strip()
            topic = employee_topics[employee_id]
            fields = [
                employee_id,
                datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
                expense["description"],
                expense["type"],
                expense["count"],
                expense["payment"],
            ]

            buffer = io.StringIO()
            csv.writer(buffer, lineterminator="\n").writerow(fields)
            message = buffer.getvalue().removesuffix("\n")

            producer.send(topic, value=message.encode("utf-8")).get(timeout=10)
            print(f"{topic} | {message}", flush=True)
    finally:
        producer.close()


if __name__ == "__main__":
    main()
