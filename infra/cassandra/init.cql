CREATE KEYSPACE IF NOT EXISTS pos
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS pos.expenses (
    user_id int,
    date_time text,
    description text,
    type text,
    count int,
    payment decimal,
    PRIMARY KEY ((user_id), date_time, description)
);
