-- SWE307 Project 3 — Draft Cassandra Schema (Task 6.4)
-- Owner: A2 (Reviewed by A3)

CREATE KEYSPACE IF NOT EXISTS pos
WITH replication = {
    'class': 'SimpleStrategy',
    'replication_factor': 1
};

CREATE TABLE IF NOT EXISTS pos.expenses (
    user_id int,
    date_time text,
    description text,
    amount decimal,
    PRIMARY KEY ((user_id), date_time, description)
);
