# Uçtan Uca (E2E) Test Planı (Taslak)

## 1. Veri Hattı Testleri (Grup A)
- **TC-01 (Kafka → Cassandra Akışı):** 4 departman topic'ine gönderilen harcama kayıtları `pos.expenses` tablosuna yazılıyor mu?
- **TC-02 (Spark Toplam Hesabı):** `ExpenseQueryService.totalExpense(7788)` (SCOTT) Cassandra'daki kayıtların doğru toplamını döndürüyor mu?
- **TC-03 (Harcaması Olmayan Çalışan):** Harcama kaydı olmayan `empno` için `0` (`BigDecimal.ZERO`) dönüyor mu?
- **TC-04 (Restart & Tekrar Kayıt):** Servisler kapatılıp açıldığında veriler korunuyor mu; tekrar işlenen kayıtlar toplamı bozuyor mu?

## 2. Web ve Sınır Durum Testleri (Grup B + E2E)
- **TC-05 (KING — Null Yönetici):** `mgr = NULL` iken `NullPointerException` oluşmadan sayfa açılıyor mu?
- **TC-06 (TURNER — Sıfır Komisyon):** `comm = 0` değeri `0` olarak korunuyor mu?
- **TC-07 (HDFS Fotoğraf):** `/photos/{ename}.jpg` yükleniyor mu; eksik dosyada 500 yerine varsayılan görsel dönüyor mu?

## 2. Web ve Sınır Durum Testleri (Grup B + E2E)
- **TC-05 (KING — Null Yönetici):** `mgr = NULL` iken `NullPointerException` oluşmadan sayfa açılıyor mu?
- **TC-06 (TURNER — Sıfır Komisyon):** `comm = 0` değeri `0` olarak korunuyor mu?
- **TC-07 (HDFS Fotoğraf):** `/photos/{ename}.jpg` yükleniyor mu; eksik dosyada 500 yerine varsayılan görsel dönüyor mu?
