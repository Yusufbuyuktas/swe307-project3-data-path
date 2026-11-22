import csv
import re
from decimal import Decimal
from pathlib import Path

from pyspark.sql import SparkSession, functions as F

SCHEMA = (
    "user_id INT, date_time STRING, description STRING, "
    "type STRING, count INT, payment DECIMAL(38,2)"
)
TOPICS = "accounting,research,sales,operations"


def parse_expense(value):
    if value is None:
        return None

    try:
        user_id, date_time, description, category, count, payment = next(
            csv.reader([value], strict=True)
        )
        user_id, count = int(user_id), int(count)
    except (csv.Error, ValueError, StopIteration):
        return None

    if not all(field.strip() for field in (date_time, description, category)):
        return None
    if not all(-(2**31) <= number < 2**31 for number in (user_id, count)):
        return None

    payment = payment.strip()
    if not re.fullmatch(r"[+-]?[0-9]{1,36}(?:\.[0-9]{1,2})?", payment):
        return None

    return user_id, date_time, description, category, count, Decimal(payment)


def write_batch(batch, _batch_id):
    (
        batch.write.format("org.apache.spark.sql.cassandra")
        .option("keyspace", "pos")
        .option("table", "expenses")
        .mode("append")
        .save()
    )


def main():
    spark = (
        SparkSession.builder.appName("SWE307 PySpark Expenses")
        .config("spark.cassandra.connection.host", "127.0.0.1")
        .getOrCreate()
    )
    spark.sparkContext.setLogLevel("WARN")

    try:
        kafka = (
            spark.readStream.format("kafka")
            .option("kafka.bootstrap.servers", "127.0.0.1:9092")
            .option("subscribe", TOPICS)
            .option("startingOffsets", "earliest")
            .load()
        )
        parse_csv = F.udf(parse_expense, SCHEMA)
        expenses = (
            kafka.select(parse_csv(F.col("value").cast("string")).alias("expense"))
            .where(F.col("expense").isNotNull())
            .select("expense.*")
        )
        checkpoint = (Path.home() / "p3-data" / "checkpoint-pyspark").as_uri()
        (
            expenses.writeStream.option("checkpointLocation", checkpoint)
            .foreachBatch(write_batch)
            .start()
            .awaitTermination()
        )
    finally:
        spark.stop()


if __name__ == "__main__":
    main()
