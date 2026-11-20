# Veri Sözleşmeleri ve Sistem Arayüzleri (Owner: A3)

> **Durum:** Taslak v0.1 — **Task 6.3 (`[BLOCKED]`)**: Hocanın POS simülatör sunucusu henüz paylaşılmadığı için simülatöre bağlı format kararları kilitli değildir.

## 1. Kafka Sözleşmesi
* **Topic Adları:** `accounting`, `research`, `sales`, `operations` (`ProjectConstants`)
* **Partition Sayısı:** Varsayılan `1` veya `3` *(A1 tarafından sabitlenecek)*.
* **Mesaj Biçimi (Value):** UTF-8 düz metin (CSV satırı).
* **Mesaj Anahtarı (Key):** `[BELİRSİZ — PENDING]`

## 2. POS CSV Alan Sırası ve Veri Tipleri (Taslak)
| Sıra | Alan | Örnek | Hedef Tip | Not |
| :--- | :--- | :--- | :--- | :--- |
| 1 | `user_id` (`empno`) | `7788` | `INT` | `bigdata.emp.empno` ile eşleşir |
| 2 | `date_time` | `2026-11-21 14:30:00` | `TEXT` | **[BELİRSİZ — PENDING]** Gerçek format simülatörden alınacak |
| 3 | `description` | `Flight Ticket` | `TEXT` | Harcama açıklaması |
| 4 | `amount` | `450.50` | `DECIMAL` | Parasal tutar |

## 3. MySQL Şema Sözleşmesi (`bigdata`)
* **Veritabanı:** `bigdata` (Referans sürüm: `8.0.46-0ubuntu0.22.04.5`, Sorumlu: **B1**)
* **`bigdata.dept`:** `deptno` (PK), `dname` (`ACCOUNTING`, `RESEARCH`, `SALES`, `OPERATIONS`), `loc`
* **`bigdata.emp`:** `empno` (PK), `ename`, `job`, `mgr` (FK → `emp.empno`, nullable), `hiredate`, `sal`, `comm` (nullable), `deptno` (FK → `dept.deptno`)

## 4. Cassandra Şema Sözleşmesi (`pos.expenses` — Taslak)
* **Keyspace:** `pos` (`SimpleStrategy`, `replication_factor = 1`) | **Tablo:** `expenses` (Sorumlu: **A2**, Onay: **A3**)
* **Şema Dosyası:** `infra/cassandra/init.cql`
* **Taslak Anahtar:** `PRIMARY KEY ((user_id), date_time, description)`
* **Not (`[BELİRSİZ — PENDING]`):** Aynı saniye ve açıklamayla gelen iki harcamanın üzerine yazılma riski ve Spark yeniden başlatmalarında idempotency konusu Task 6.3 çözüldüğünde A2 ile kesinleştirilecektir.

## 5. HDFS ve Java Ortak Sözleşmeleri
* **HDFS Fotoğraf Yolu:** `/photos/{filename}` (`ProjectConstants.HDFS_PHOTOS_DIR`, Sorumlu: **B2**). Eksik görselde 500 yerine placeholder döner.
* **`ExpenseQueryService` (A2):** `BigDecimal totalExpense(int empno)` — Kayıt yoksa `BigDecimal.ZERO` (`0`) döner.
* **`PhotoService` (B2):** `byte[] getPhoto(String fileName)`
* **`EmployeeRow` (A3):** `imageFile`, `ename`, `mgrName`, `salary`, `commission`, `department`, `totalExpense`.

## 6. Null Değerler ve Hata Davranışları
* **Yöneticisi olmayan (`KING`):** `mgr` `NULL` ise `-` veya `NULL` gösterilir, NPE oluşmaz.
* **Komisyonu olmayan / 0 olan (`TURNER` = 0, `SCOTT` = NULL):** `0` değeri korunur, `NULL` hatasız işlenir.
* **Harcaması olmayan çalışan:** `totalExpense` alanı `0` (`0.00`) gösterilir.
* **Olmayan personel araması:** Kullanıcı dostu mesaj döner, stack trace gösterilmez.

## 7. Açık / Kesinleşmemiş Maddeler (Task 6.2 & Task 6.3 [BLOCKED])
- [ ] **[BLOCKED — POS Simülatörü Bekleniyor]** POS tarih formatı
- [ ] **[BLOCKED — POS Simülatörü Bekleniyor]** POS'un Kafka'ya nasıl bağlandığı
- [ ] **[BLOCKED — POS Simülatörü Bekleniyor]** Topic yönlendirmesini simülatörün yapıp yapmadığı
- [ ] **[PENDING]** Kafka mesaj key'i
- [ ] **[PENDING — A2 & A3]** Cassandra'da tekrar kayıtların nasıl önleneceği (Primary Key tasarımı)
