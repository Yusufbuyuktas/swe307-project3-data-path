# A2 — `spark-query` Sorgu Doğrulama ve Süre Ölçümleri

## Test Ortamı ve Veri Özeti (22 Kasım 2026)
- **Tablo:** `pos.expenses` (elle eklenen 2 test satırı)
- **Doğrulama (`cqlsh`):** `SELECT SUM(amount) FROM pos.expenses WHERE user_id = 7788;` → `150.75`
- **Spark yapılandırması:** `local[2]`, `spark.ui.enabled=false`, `spark.sql.shuffle.partitions=2`, `-Xmx1g`

## `QueryDemo` Ölçüm Sonuçları

| Çağrı | `user_id` | Hesaplanan Toplam | Süre (ms) | Not |
| :--- | :---: | :---: | :---: | :--- |
| Çağrı 1 | 7788 | 150.75 | 4161 | İlk çağrı (`SparkSession` açılışı dahil) |
| Çağrı 2 | 7788 | 150.75 | 201 | `cqlsh` ile birebir aynı |
| Çağrı 3 | 7788 | 150.75 | 202 | `cqlsh` ile birebir aynı |
| Çağrı 4 | 7788 | 150.75 | 208 | `cqlsh` ile birebir aynı |
| Çağrı 5 | 7788 | 150.75 | 159 | `cqlsh` ile birebir aynı |
| Çağrı 6 | 7788 | 150.75 | 227 | `cqlsh` ile birebir aynı |
| Kayıtsız | 9999 | 0.00 | 164 | Hata fırlatmadan `0.00` döndü |

- **2.–6. çağrı ortalaması:** 199.4 ms

## M7 — Canlı Veri Akışı Doğrulaması (22 Kasım 2026)
- **Tablo:** `pos.expenses` (6 kolonlu kesin şema, toplam 21 kayıt)
- **Doğrulama (`cqlsh`):** `SELECT SUM(payment) FROM pos.expenses WHERE user_id = 7788;` → `1280.45`

| Çağrı | `user_id` | Hesaplanan Toplam | Süre (ms) | Not |
| :--- | :---: | :---: | :---: | :--- |
| Çağrı 1 | 7788 | 1280.45 | 4699 | İlk çağrı (`SparkSession` açılışı dahil) |
| Çağrı 2 | 7788 | 1280.45 | 241 | `cqlsh` ile birebir aynı |
| Çağrı 3 | 7788 | 1280.45 | 195 | `cqlsh` ile birebir aynı |
| Çağrı 4 | 7788 | 1280.45 | 229 | `cqlsh` ile birebir aynı |
| Çağrı 5 | 7788 | 1280.45 | 246 | `cqlsh` ile birebir aynı |
| Çağrı 6 | 7788 | 1280.45 | 171 | `cqlsh` ile birebir aynı |
| Kayıtsız | 9999 | 0.00 | 197 | Hata fırlatmadan `0.00` döndü |

- **2.–6. çağrı ortalaması:** 216.4 ms
