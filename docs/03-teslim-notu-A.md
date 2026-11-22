# Grup A Teslim Notu (v0.1-veri-hatti)

## 1. Tamamlanan Bileşenler
- **POS & Kafka Akışı (`scripts/pos_producer.py`):** 6 kolonlu (`user_id, date_time, description, type, count, payment`) POS kayıtlarını departman topic'lerine (`accounting`, `research`, `sales`, `operations`) iletir.
- **Streaming & Cassandra Yazımı (`streaming-job` / `scripts/stream_expenses.py`):** Kafka kayıtlarını ayrıştırıp Cassandra `pos.expenses` tablosuna yazar.
- **Spark Sorgu Servisi (`spark-query` — `SparkExpenseQueryService`):** `ExpenseQueryService.totalExpense(int empno)` sözleşmesini gerçekleştirir; `user_id = empno` filtresiyle `SUM(payment)` sonucunu `BigDecimal` olarak döndürür (kayıt yoksa `0`).

## 2. Başlatma Sırası
1. Zookeeper, Kafka (`4` topic), Cassandra (`UN`) ve Spark servislerini başlatın.
2. Şema kurulumu: `cqlsh -f infra/cassandra/init.cql`
3. Streaming işini başlatın (`StreamingJobApplication` veya `scripts/stream_expenses.py`).
4. POS üreticisini çalıştırın (`scripts/pos_producer.py`).

## 3. Grup B Entegrasyon Bilgisi
- `web-app` modülü `SparkExpenseQueryService` üzerinden `ExpenseQueryService` arayüzünü doğrudan kullanabilir.
- Cassandra bağlantısı: `127.0.0.1:9042`, keyspace `pos`, tablo `expenses` (`user_id` kolonu `emp.empno` ile eşleşir).
