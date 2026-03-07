# 📝 Latihan Soal — Bab 8: Business Object & Business Process

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Kembali ke Beranda](../README.md) · [📖 Materi Business Object](../docs/08-business-object-process.md)

---

## Petunjuk

- Pilih **satu jawaban** yang paling tepat untuk setiap soal.
- Setiap soal berbasis **skenario** — baca kasus dengan cermat.
- Kunci jawaban tersedia di [bagian bawah](#-kunci-jawaban).

---

### Soal 1 — Kasus: Anemic vs Rich Domain Model 🤖

Perhatikan dua versi class `Invoice`:

**Versi A:**
```java
class Invoice {
    public double total;
    public String status;
}
// Validasi dan kalkulasi dilakukan di InvoiceService
```

**Versi B:**
```java
class Invoice {
    private double total;
    private String status;

    public void approve() {
        if (total <= 0) throw new IllegalStateException("Total harus > 0");
        this.status = "APPROVED";
    }
}
```

Versi mana yang merupakan **Rich Domain Model**, dan mengapa?

- **A.** Versi A — karena field-nya `public` sehingga lebih fleksibel
- **B.** Versi B — karena objek menyimpan aturan bisnisnya sendiri (self-validating, perilaku di dalam objek)
- **C.** Versi A — karena logika bisnis dipisahkan ke service
- **D.** Keduanya sama saja, hanya beda gaya penulisan

---

### Soal 2 — Kasus: Self-Validating Object ✅

Seorang developer membuat class `PurchaseOrder` yang menerima jumlah item negatif tanpa error. Validasi jumlah hanya dilakukan di `PurchaseOrderController`.

Apa masalah dari pendekatan ini?

- **A.** Tidak ada masalah — validasi di Controller sudah cukup
- **B.** Jika PO dibuat dari tempat lain (misal: import CSV, API lain), validasi Controller terlewat — PO invalid masuk ke sistem
- **C.** Java tidak mendukung validasi di class domain
- **D.** Controller seharusnya tidak ada

---

### Soal 3 — Kasus: PO yang Sudah Di-submit 📋

Perhatikan aturan bisnis berikut:
> Setelah PO di-submit, tidak boleh ada perubahan item.

Developer menulis:
```java
class PurchaseOrder {
    private String status = "DRAFT";
    private List<POItem> items = new ArrayList<>();

    public void tambahItem(POItem item) {
        if (!"DRAFT".equals(status)) {
            throw new IllegalStateException("PO sudah di-submit");
        }
        items.add(item);
    }
}
```

Pendekatan ini menunjukkan prinsip apa?

- **A.** Polymorphism — karena ada pengecekan tipe
- **B.** Invariant Protection — objek menjaga aturan bisnisnya sendiri, mencegah state yang tidak valid
- **C.** Observer Pattern — karena status di-monitor
- **D.** Singleton — karena hanya satu PO yang boleh di-submit

---

### Soal 4 — Kasus: Application Service vs Domain Logic 🏗️

Manakah yang **sebaiknya** menjadi tanggung jawab Application Service (bukan domain object)?

- **A.** Menghitung total pesanan
- **B.** Memvalidasi bahwa stok harus positif
- **C.** Mengorkestrasikan alur: ambil PO → validasi → simpan → kirim notifikasi
- **D.** Menentukan apakah PO perlu approval

---

### Soal 5 — Kasus: Batas Approval 💼

Sebuah perusahaan punya aturan:
- PO di bawah Rp 50 juta → otomatis disetujui
- PO Rp 50 juta ke atas → perlu approval manajer

Developer menaruh logika ini di `PurchaseOrderController`. Apa rekomendasi terbaik?

- **A.** Biarkan di Controller — itu tempat yang tepat
- **B.** Pindahkan ke database trigger
- **C.** Pindahkan ke dalam class `PurchaseOrder` sebagai method `perluApproval()` — aturan batas ini adalah aturan bisnis domain
- **D.** Buat class terpisah bernama `ApprovalChecker` yang tidak berhubungan dengan PO

---

### Soal 6 — Kasus: Object yang "Tahu Dirinya" 🪞

Perhatikan kode:
```java
class KeranjangBelanja {
    private List<Item> items = new ArrayList<>();
    private static final double BATAS_ONGKIR_GRATIS = 500_000;

    public double hitungTotal() {
        return items.stream().mapToDouble(Item::getHarga).sum();
    }

    public boolean gratisOngkir() {
        return hitungTotal() >= BATAS_ONGKIR_GRATIS;
    }
}
```

Mengapa pendekatan ini lebih baik daripada menaruh logika `gratisOngkir()` di service terpisah?

- **A.** Karena Java memaksa semua logika di satu class
- **B.** Karena objek yang "pintar" menjaga konsistensi — aturan gratis ongkir tidak bisa terlewat meskipun diakses dari mana pun
- **C.** Service terpisah tidak bisa mengakses field `items`
- **D.** Tidak ada bedanya — keduanya sama-sama benar

---

### Soal 7 — Kasus: Tell, Don't Ask 🗣️

Prinsip "Tell, Don't Ask" mengatakan: **beritahu objek apa yang harus dilakukan, jangan minta datanya lalu kerjakan di luar**.

Mana kode yang **melanggar** prinsip ini?

- **A.** `pesanan.konfirmasi();`
- **B.** `if (pesanan.getStatus().equals("BARU")) { pesanan.setStatus("DIKONFIRMASI"); }`
- **C.** `pesanan.tambahItem(item);`
- **D.** `pesanan.hitungTotal();`

---

### Soal 8 — Kasus: Orkestrator Proses Bisnis 🎵

Sebuah proses pengadaan barang melibatkan langkah:
1. Validasi PO
2. Cek budget departemen
3. Simpan PO ke database
4. Kirim notifikasi ke vendor

Siapa yang **sebaiknya** mengorkestrasikan langkah-langkah ini?

- **A.** Class `PurchaseOrder` sendiri — karena semua logika harus di domain
- **B.** Application Service (`PengadaanService`) — karena orkestrasi proses melibatkan koordinasi antar komponen, bukan aturan bisnis internal satu objek
- **C.** Database stored procedure
- **D.** REST Controller

---

### Soal 9 — Kasus: Immutable Value dalam PO 🔒

Dalam sebuah PO, harga satuan sebuah item tidak boleh berubah setelah dicatat. Developer membuat `POItem` sebagai berikut:

```java
class POItem {
    private final String nama;
    private final int jumlah;
    private final double hargaSatuan;

    public POItem(String nama, int jumlah, double hargaSatuan) {
        this.nama = nama;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
    }

    // Hanya getter, tidak ada setter
}
```

Pendekatan ini menerapkan:

- **A.** Observer Pattern
- **B.** Immutability — objek tidak bisa diubah setelah dibuat, menjamin integritas data harga
- **C.** Singleton Pattern
- **D.** Lazy Loading

---

### Soal 10 — Kasus: Identifikasi Tanggung Jawab 🧩

Tentukan apakah tanggung jawab berikut ada di **Domain Object (D)** atau **Application Service (S)**:

| No | Tanggung Jawab | Tempat |
|:--:|:---------------|:------:|
| 1  | Memvalidasi total PO > 0 | ? |
| 2  | Menyimpan PO ke database | ? |
| 3  | Menghitung subtotal item | ? |
| 4  | Mengirim email notifikasi | ? |

Urutan yang benar:

- **A.** D, D, D, D
- **B.** D, S, D, S
- **C.** S, S, D, S
- **D.** S, D, S, D

---

## ✅ Kunci Jawaban

<details>
<summary><b>Klik untuk melihat jawaban</b></summary>

| Soal | Jawaban | Penjelasan Singkat |
|:----:|:-------:|:-------------------|
| 1 | **B** | Rich Domain Model menempatkan aturan bisnis (validasi, state transition) di dalam objek domain itu sendiri, bukan di service terpisah. |
| 2 | **B** | Self-validating object memastikan data valid di mana pun objek dibuat. Validasi hanya di Controller rentan terlewat jika ada jalur pembuatan lain. |
| 3 | **B** | Objek menjaga invariant-nya sendiri: PO yang sudah di-submit tidak akan pernah bisa ditambah item, terlepas dari siapa yang memanggilnya. |
| 4 | **C** | Application Service mengorkestrasikan alur proses lintas komponen (repository, notifikasi). Kalkulasi total dan validasi stok adalah aturan domain. |
| 5 | **C** | Aturan batas approval adalah aturan bisnis yang melekat pada PO. Meletakkannya di Controller membuat aturan rentan tidak konsisten. |
| 6 | **B** | Objek yang menyimpan aturan bisnisnya sendiri mencegah inkonsistensi — aturan gratis ongkir selalu diperiksa dengan data yang tepat. |
| 7 | **B** | Kode ini "meminta data" (getStatus), lalu "mengerjakan di luar" (setStatus). Seharusnya cukup `pesanan.konfirmasi()` — biarkan objek mengurus internalnya sendiri. |
| 8 | **B** | Application Service mengorkestrasikan langkah-langkah proses bisnis. Domain object fokus pada aturan bisnis internal satu entitas. |
| 9 | **B** | Field `final` + tidak ada setter = objek immutable. Harga item yang sudah dicatat tidak bisa berubah, menjamin integritas data historis. |
| 10 | **B** | Validasi (D) dan kalkulasi (D) adalah aturan domain. Simpan ke DB (S) dan kirim email (S) adalah orkestrasi infrastruktur oleh service. |

</details>

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📝 Quiz Hexagonal](07-quiz-hexagonal.md) | [📝 Quiz Workflow Engine →](09-quiz-workflow.md) |

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
