# 📦 Bab 8: Business Object & Business Process

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Hexagonal](07-hexagonal-architecture.md) · [⬅️ Beranda](../README.md) · [Workflow Engine ➡️](09-workflow-engine.md)

---

## 📌 Apa Itu Business Object?

**Business Object** adalah representasi **objek nyata dalam bisnis** yang dimodelkan sebagai class di dalam program. Bukan sekadar wadah data (POJO/DTO), melainkan objek yang **memiliki perilaku bisnis** — ia tahu aturan bisnisnya sendiri.

> **Inti dari Business Object:** _"Objek bisnis bukan sekadar 'tas kosong' penampung data. Ia harus cerdas — tahu cara memvalidasi dirinya, menghitung bisnisnya, dan menjaga integritasnya."_

---

## 🧠 Analogi Dunia Nyata

### 📋 Formulir Pembelian (Purchase Order)

Di kantor, saat kamu ingin membeli barang, ada **formulir PO (Purchase Order)**:

```
┌──────────────────────────────────────────────┐
│           PURCHASE ORDER                      │
│           No: PO-2026-0001                   │
├──────────────────────────────────────────────┤
│ Pemohon    : Budi Santoso                    │
│ Departemen : IT                              │
│ Tanggal    : 7 Maret 2026                    │
├──────────────────────────────────────────────┤
│ No │ Barang          │ Qty │ Harga    │ Total│
│  1 │ Laptop Dell     │  5  │ 15.000.000│75jt │
│  2 │ Mouse Wireless  │ 10  │    250.000│2.5jt│
├──────────────────────────────────────────────┤
│ Grand Total: Rp 77.500.000                   │
├──────────────────────────────────────────────┤
│ Status: MENUNGGU PERSETUJUAN                 │
│                                              │
│ Aturan:                                      │
│ • Total > 50jt harus disetujui Direktur      │
│ • Total ≤ 50jt cukup disetujui Manager       │
│ • Tidak boleh tambah item setelah disetujui  │
└──────────────────────────────────────────────┘
```

Formulir ini **bukan sekadar kertas** — ia punya **aturan bisnis**:
- Siapa yang harus approve tergantung total
- Tidak bisa diubah setelah disetujui
- Harus punya minimal 1 item

**Business Object** di kode harus se-cerdas formulir ini.

---

## 🔑 Perbedaan: Data Object vs Business Object

```
❌ DATA OBJECT (Anemic):              ✅ BUSINESS OBJECT (Rich):
──────────────────────                ──────────────────────────
class PO {                            class PurchaseOrder {
    public String nomor;                  private String nomor;
    public List items;                    private List<POItem> items;
    public String status;                 private StatusPO status;
    public double total;
                                          // PERILAKU BISNIS:
    // Tidak ada logika!                  public void tambahItem(...)
    // Semua logic di luar                public void ajukanPersetujuan()
}                                         public Uang hitungTotal()
                                          public boolean perluApprovalDirektur()
Masalah:                                  // Aturan hidup di DALAM objek
• Logic tersebar di mana-mana         }
• Objek bisa diisi sembarangan
• Validasi mudah terlewat             Keuntungan:
                                      • Logic terpusat di objek
                                      • Self-validating
                                      • Mudah dipahami & dimaintain
```

---

## 💻 Contoh Lengkap: Sistem Purchase Order

```java
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ═══════════════════════════════════════════════
 * VALUE OBJECT: Item dalam Purchase Order
 * ═══════════════════════════════════════════════
 */
class POItem {
    private final String namaBarang;
    private final int qty;
    private final double hargaSatuan;

    public POItem(String namaBarang, int qty, double hargaSatuan) {
        if (namaBarang == null || namaBarang.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama barang wajib diisi");
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("Qty harus lebih dari 0");
        }
        if (hargaSatuan <= 0) {
            throw new IllegalArgumentException("Harga harus lebih dari 0");
        }
        this.namaBarang = namaBarang.trim();
        this.qty = qty;
        this.hargaSatuan = hargaSatuan;
    }

    public double getSubtotal() {
        return qty * hargaSatuan;
    }

    public String getNamaBarang() { return namaBarang; }
    public int getQty() { return qty; }
    public double getHargaSatuan() { return hargaSatuan; }

    @Override
    public String toString() {
        return String.format("%-20s x%d @ Rp %,.0f = Rp %,.0f",
            namaBarang, qty, hargaSatuan, getSubtotal());
    }
}

/**
 * ═══════════════════════════════════════════════
 * BUSINESS OBJECT: Purchase Order
 *
 * Objek ini CERDAS — ia tahu aturan bisnisnya:
 * • Minimal 1 item
 * • Tidak bisa diubah setelah disetujui
 * • Total > 50jt perlu approval direktur
 * • Hanya status tertentu yang bisa di-approve
 * ═══════════════════════════════════════════════
 */
class PurchaseOrder {

    // ── Constants ──
    private static final double BATAS_APPROVAL_DIREKTUR = 50_000_000;

    // ── Identity ──
    private final String nomorPO;
    private final String pemohon;
    private final String departemen;
    private final LocalDate tanggal;

    // ── State ──
    private List<POItem> items;
    private String status;

    // ── Constructor ──
    public PurchaseOrder(String nomorPO, String pemohon, String departemen) {
        if (nomorPO == null || nomorPO.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor PO wajib diisi");
        }
        this.nomorPO = nomorPO;
        this.pemohon = pemohon;
        this.departemen = departemen;
        this.tanggal = LocalDate.now();
        this.items = new ArrayList<>();
        this.status = "DRAFT";
    }

    // ════════════════════════════════════
    // BUSINESS BEHAVIOR (bukan sekadar getter/setter!)
    // ════════════════════════════════════

    /**
     * Tambah item — hanya bisa saat status DRAFT
     */
    public void tambahItem(String namaBarang, int qty, double hargaSatuan) {
        pastikanStatus("DRAFT", "menambah item");
        items.add(new POItem(namaBarang, qty, hargaSatuan));
        System.out.println("  ✅ Item ditambahkan: " + namaBarang);
    }

    /**
     * Hitung grand total dari semua item
     */
    public double hitungTotal() {
        double total = 0;
        for (POItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Cek apakah PO ini perlu approval level direktur
     */
    public boolean perluApprovalDirektur() {
        return hitungTotal() > BATAS_APPROVAL_DIREKTUR;
    }

    /**
     * Ajukan persetujuan — validasi bisnis diterapkan di sini
     */
    public void ajukanPersetujuan() {
        pastikanStatus("DRAFT", "mengajukan persetujuan");

        if (items.isEmpty()) {
            throw new IllegalStateException(
                "❌ PO harus punya minimal 1 item!");
        }

        this.status = "MENUNGGU_PERSETUJUAN";
        String levelApproval = perluApprovalDirektur()
            ? "DIREKTUR" : "MANAGER";

        System.out.println("📤 PO " + nomorPO + " diajukan.");
        System.out.println("   Total: Rp " + formatRupiah(hitungTotal()));
        System.out.println("   Perlu approval: " + levelApproval);
    }

    /**
     * Setujui PO
     */
    public void setujui(String approver) {
        pastikanStatus("MENUNGGU_PERSETUJUAN", "menyetujui");
        this.status = "DISETUJUI";
        System.out.println("✅ PO " + nomorPO + " disetujui oleh " + approver);
    }

    /**
     * Tolak PO
     */
    public void tolak(String approver, String alasan) {
        pastikanStatus("MENUNGGU_PERSETUJUAN", "menolak");
        this.status = "DITOLAK";
        System.out.println("❌ PO " + nomorPO + " ditolak oleh " + approver);
        System.out.println("   Alasan: " + alasan);
    }

    // ════════════════════════════════════
    // INTERNAL HELPERS
    // ════════════════════════════════════

    private void pastikanStatus(String statusYangDiharapkan, String aksi) {
        if (!this.status.equals(statusYangDiharapkan)) {
            throw new IllegalStateException(
                "❌ Tidak bisa " + aksi + " — status saat ini: " + status
                + " (dibutuhkan: " + statusYangDiharapkan + ")");
        }
    }

    private String formatRupiah(double jumlah) {
        return String.format("%,.0f", jumlah);
    }

    // ════════════════════════════════════
    // READ-ONLY ACCESS
    // ════════════════════════════════════

    public String getNomorPO() { return nomorPO; }
    public String getStatus() { return status; }
    public List<POItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void tampilkan() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  PURCHASE ORDER: " + nomorPO);
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  Pemohon    : " + pemohon);
        System.out.println("║  Departemen : " + departemen);
        System.out.println("║  Tanggal    : " + tanggal);
        System.out.println("║  Status     : " + status);
        System.out.println("╠════════════════════════════════════════╣");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("║  " + (i + 1) + ". " + items.get(i));
        }
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  TOTAL: Rp " + formatRupiah(hitungTotal()));
        System.out.println("╚════════════════════════════════════════╝");
    }
}
```

---

## 🔄 Business Process — Orkestrasi Objek Bisnis

**Business Process** adalah **alur kerja** yang mengorkestrasi beberapa Business Object untuk menyelesaikan satu proses bisnis end-to-end.

### Analogi: Proses Pengadaan Barang

```
 ┌────────┐     ┌───────────┐     ┌──────────┐     ┌──────────┐
 │ Buat PO│────►│Ajukan     │────►│ Approve  │────►│ Terima   │
 │        │     │Persetujuan│     │ / Reject │     │ Barang   │
 └────────┘     └───────────┘     └──────────┘     └──────────┘
     │               │                │                  │
   📦 PO           📦 PO           📦 PO              📦 PO
   (Draft)     (Menunggu)       (Disetujui)        (Selesai)
```

### Contoh: Application Service (Process Orchestrator)

```java
/**
 * APPLICATION SERVICE: Mengorkestrasi proses pengadaan
 * Bukan logika bisnis — hanya koordinasi antar objek
 */
class PengadaanService {

    private final PurchaseOrderRepository poRepo;
    private final NotifikasiService notifService;

    public PengadaanService(PurchaseOrderRepository poRepo,
                            NotifikasiService notifService) {
        this.poRepo = poRepo;
        this.notifService = notifService;
    }

    /**
     * Proses: Buat PO baru → Tambah item → Ajukan
     */
    public PurchaseOrder buatDanAjukanPO(String pemohon, String dept,
                                          List<POItem> items) {
        // 1. Buat PO
        String nomor = "PO-" + System.currentTimeMillis();
        PurchaseOrder po = new PurchaseOrder(nomor, pemohon, dept);

        // 2. Tambah semua item
        for (POItem item : items) {
            po.tambahItem(item.getNamaBarang(),
                          item.getQty(),
                          item.getHargaSatuan());
        }

        // 3. Ajukan persetujuan (logika bisnis ADA DI PO)
        po.ajukanPersetujuan();

        // 4. Simpan
        poRepo.simpan(po);

        // 5. Notifikasi approver yang tepat
        if (po.perluApprovalDirektur()) {
            notifService.notifDirektur(nomor);
        } else {
            notifService.notifManager(nomor);
        }

        return po;
    }

    /**
     * Proses: Approve PO
     */
    public void approvePO(String nomorPO, String approver) {
        PurchaseOrder po = poRepo.cariByNomor(nomorPO)
            .orElseThrow(() -> new RuntimeException("PO tidak ditemukan"));

        po.setujui(approver);  // Logika bisnis ada di PO
        poRepo.simpan(po);
        notifService.notifPemohon(nomorPO, "disetujui");
    }
}
```

---

## 🔍 Prinsip Penting

### 1. Logika Bisnis di DALAM Objek

```
❌ SALAH (Anemic Domain Model):
─────────────────────────────
// Logic di Service, objek hanya data
if (po.getStatus().equals("DRAFT") && po.getItems().size() > 0) {
    po.setStatus("MENUNGGU");
}

✅ BENAR (Rich Domain Model):
─────────────────────────────
// Logic di dalam objek bisnis
po.ajukanPersetujuan();  // PO sendiri yang validasi & ubah status
```

### 2. Self-Validating Object

```java
// Objek tidak pernah berada dalam kondisi tidak valid
class PurchaseOrder {
    public void tambahItem(...) {
        pastikanStatus("DRAFT", "menambah item");  // ← Validasi
        // ...
    }

    public void ajukanPersetujuan() {
        pastikanStatus("DRAFT", "mengajukan");     // ← Validasi
        if (items.isEmpty()) { ... }                // ← Validasi
        // ...
    }
}
```

### 3. Application Service vs Domain Logic

| Tanggung Jawab | Siapa? | Contoh |
|:---------------|:-------|:-------|
| Validasi bisnis | **Business Object** | PO cek apakah bisa di-approve |
| Hitung total | **Business Object** | PO.hitungTotal() |
| Koordinasi antar objek | **Application Service** | Simpan, notifikasi |
| Transaksi database | **Application Service** | Begin/commit transaction |

---

## 📋 Checklist Business Object

- [ ] Business Object punya **behavior** (method bisnis), bukan hanya getter/setter
- [ ] **Validasi** ada di dalam objek (self-validating)
- [ ] **Status/state** diubah lewat method bisnis, bukan setter langsung
- [ ] Objek **tidak bisa** berada dalam kondisi tidak valid
- [ ] Application Service hanya **mengorkestrasi**, bukan membuat keputusan bisnis

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📖 ← Hexagonal](07-hexagonal-architecture.md) | [📖 Workflow Engine →](09-workflow-engine.md) |

---

<p align="center"><i>"Business Object yang baik seperti karyawan yang cerdas — ia tahu aturannya sendiri tanpa harus diingatkan terus."</i></p>

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
