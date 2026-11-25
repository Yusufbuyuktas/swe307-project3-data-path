# Uçtan Uca (E2E) Test Planı ve Doğrulama Raporu

- **Test Tarihi:** 2026-11-25
- **Test Ortamı:** Ubuntu 22.04.5 LTS (WSL2) | Java 11.0.33.1 | Maven 3.8.6
- **Altyapı Bileşenleri:** MySQL 8.0.46 (`bigdata`), Cassandra 4.1.3 (`pos.expenses`), HDFS 3.3.6 (`/photos`), Kafka 3.5.1 (4 topic), Spark 3.3.3 (`local[2]`), Spring Boot 2.7.18 (`--spring.profiles.active=real`)

## 1. Veri Hattı Testleri (Grup A)

| Test ID | Senaryo | Doğrulama Adımı ve Gözlenen Sonuç | Durum |
| --- | --- | --- | --- |
| **TC-01** | **Kafka → Cassandra Canlı Akışı** | `scripts/pos_producer.py` ile `accounting`, `research`, `sales`, `operations` topic'lerine gönderilen POS mesajları `scripts/stream_expenses.py` (PySpark) tarafından `pos.expenses` tablosuna yazıldı; canlı akışta `SCOTT` toplamı `4560.00` → `5920,45` olarak güncellendi. | **GEÇTİ** |
| **TC-02** | **Spark Toplam Hesabı** | `ExpenseQueryService.totalExpense(7788)` (`SCOTT`) Cassandra'daki çoklu harcama kayıtlarının toplamını doğru hesaplayarak arayüze aktardı. | **GEÇTİ** |
| **TC-03** | **Harcaması Olmayan Çalışan** | Cassandra'da harcama kaydı bulunmayan `JAMES` (`7900`) sorgulandığında hata fırlatılmadan `BigDecimal.ZERO` (`0.00`) döndü. | **GEÇTİ** |
| **TC-04** | **Restart & Checkpoint Devamlılığı** | PySpark durdurulup Kafka'ya kayıt gönderildikten sonra `~/p3-data/checkpoint-pyspark` korunarak yeniden başlatıldığında biriken 10/10 kayıt kayıpsız işlendi. | **GEÇTİ** |

## 2. Web ve Sınır Durum Testleri (Grup B + E2E)

| Test ID | Senaryo | Doğrulama Adımı ve Gözlenen Sonuç | Durum |
| --- | --- | --- | --- |
| **TC-05** | **`KING` — Null Yönetici (`mgr = NULL`)** | `NullPointerException` oluşmadan Yönetici sütununda `-` gösterildi. | **GEÇTİ** |
| **TC-06** | **`TURNER` — Sıfır Komisyon (`comm = 0`)** | Komisyon değeri `-` ile karıştırılmadan `0` olarak korundu. | **GEÇTİ** |
| **TC-07** | **HDFS Fotoğraf ve Yedek Görsel** | `/photos/scott.jpg` HDFS üzerinden yüklendi; eksik dosya durumunda 500 hatası yerine varsayılan görsel döndü. | **GEÇTİ** |
| **TC-08** | **Kayıtsız / Boş Arama Yönetimi** | Kayıtsız isim aramasında 404/500 hata sayfası yerine kullanıcı bulunamadı uyarısı gösterildi; boş aramada sayfa hatasız form ekranında kaldı. | **GEÇTİ** |

## 3. Otomatik Test Doğrulaması

- Tüm modüllerde birim ve sözleşme testleri `mvn clean verify` komutu ile hatasız çalıştırıldı (27 test, 0 hata).
