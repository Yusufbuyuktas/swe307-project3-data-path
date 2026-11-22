# Grup A Teslim Notu (v0.1-veri-hatti — Taslak)

## 1. Kapsam
- Kafka (`accounting`, `research`, `sales`, `operations`) → Spark Structured Streaming (`streaming-job`) → Cassandra (`pos.expenses`) veri hattı.
- `spark-query` modülü üzerinden `ExpenseQueryService.totalExpense(int empno)` sorgu servisi.

## 2. Başlatma Sırası
1. Zookeeper, Kafka, Cassandra servislerinin ayakta olduğunu doğrula (`UN` ve 4 topic).
2. `infra/cassandra/init.cql` şemasını uygula (`cqlsh -f infra/cassandra/init.cql`).
3. `streaming-job` uygulamasını başlat.
4. POS simülatörünü (veya örnek producer'ı) çalıştır.

## 3. Bilinen Kısıtlar
- POS simülatör çıktısı ve `payment` / `TopicRouter` detayı doğrulandıktan sonra v0.1 etiketi kesilecektir.
