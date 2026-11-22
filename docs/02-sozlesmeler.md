# Veri Sözleşmeleri ve Sistem Arayüzleri (Owner: A3)

## 1. POS CSV Formatı (6 Kolon — Kesinleşti)
`<user_id, date_time, description, type, count, payment>`
* `user_id` (`int`): `bigdata.emp.empno` (örn. `7788`)
* `date_time` (`text`): İşlem zaman damgası (örn. `2026-11-22 14:10:00`)
* `description` (`text`): Harcama açıklaması
* `type` (`text`): Harcama türü/kategorisi (örn. `Hardware`, `Travel`, `Food`)
* `count` (`int`): Adet bilgisi
* `payment` (`decimal`): Kredi kartından çekilen işlem tutarı (virgülden sonra 2 basamak). `totalExpense(empno)` doğrudan `SUM(payment)` hesaplar.

## 2. Kafka Topic Yönlendirmesi
* Topic'ler: `accounting`, `research`, `sales`, `operations`
* Kayıtlar çalışanın departmanına göre ilgili topic'e yönlendirilir (`7788` SCOTT → `research`).
* Hocanın simülatörü doğrudan departman topic'ine yazmazsa producer/bridge katmanında `TopicRouter` ile `empno -> dname` eşlemesi yapılacaktır.

## 3. Cassandra Şeması (`infra/cassandra/init.cql`)
* Keyspace: `pos` | Tablo: `expenses`
* Kolonlar: `user_id int`, `date_time text`, `description text`, `type text`, `count int`, `payment decimal`
* Anahtar: `PRIMARY KEY ((user_id), date_time, description)` (Simülatör saniyede 1 kayıt ürettiği için saniye + açıklama bazında tekildir).

## 4. Açık Kalan Tek Madde (`[BLOCKED]`)
* Hocanın paylaşacağı starter GitHub reposu (POS simülatör sunucusu + çalışan fotoğrafları + örnek CSV).
