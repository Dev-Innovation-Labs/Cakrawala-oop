/**
 * ═══════════════════════════════════════════════════════════
 *  BAB 8: BUSINESS OBJECT & BUSINESS PROCESS
 * ═══════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Business Object yang "pintar" (self-validating)
 *  • Rich Domain Model vs Anemic Domain Model
 *  • Business Process lewat Application Service
 *  • Aturan bisnis di dalam objek, bukan tersebar
 *
 *  Compile & Run:
 *    javac PurchaseOrder.java
 *    java PurchaseOrder
 * ═══════════════════════════════════════════════════════════
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// ═══════════════════════════════════════════
// VALUE OBJECT: POItem
// ═══════════════════════════════════════════
class POItem {
    private final String namaBarang;
    private final int jumlah;
    private final double hargaSatuan;

    public POItem(String namaBarang, int jumlah, double hargaSatuan) {
        if (namaBarang == null || namaBarang.isBlank())
            throw new IllegalArgumentException("Nama barang wajib diisi");
        if (jumlah <= 0)
            throw new IllegalArgumentException("Jumlah harus > 0");
        if (hargaSatuan <= 0)
            throw new IllegalArgumentException("Harga wajib > 0");
        this.namaBarang = namaBarang;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
    }

    public double getSubtotal() { return jumlah * hargaSatuan; }
    public String getNamaBarang() { return namaBarang; }
    public int getJumlah() { return jumlah; }
    public double getHargaSatuan() { return hargaSatuan; }

    @Override
    public String toString() {
        return String.format("%-20s x%-3d @ %,.0f = %,.0f",
            namaBarang, jumlah, hargaSatuan, getSubtotal());
    }
}

// ═══════════════════════════════════════════
// BUSINESS OBJECT: PurchaseOrder (Rich Domain Model)
// ═══════════════════════════════════════════
class PurchaseOrderBO {
    // --- Identitas
    private final String nomorPO;
    private final String vendor;
    private final LocalDate tanggal;

    // --- State
    private final List<POItem> items = new ArrayList<>();
    private String status;

    // --- Aturan Bisnis
    private static final double BATAS_APPROVAL = 50_000_000;
    private static final int MAKS_ITEM = 20;

    public PurchaseOrderBO(String nomorPO, String vendor) {
        if (nomorPO == null || nomorPO.isBlank())
            throw new IllegalArgumentException("Nomor PO wajib diisi");
        if (vendor == null || vendor.isBlank())
            throw new IllegalArgumentException("Vendor wajib diisi");
        this.nomorPO = nomorPO;
        this.vendor = vendor;
        this.tanggal = LocalDate.now();
        this.status = "DRAFT";
    }

    // ─── Perilaku bisnis: Tambah Item ───
    public void tambahItem(String nama, int jumlah, double harga) {
        validasiDraft();
        if (items.size() >= MAKS_ITEM) {
            throw new IllegalStateException(
                "Maksimum " + MAKS_ITEM + " item per PO");
        }
        items.add(new POItem(nama, jumlah, harga));
    }

    // ─── Perilaku bisnis: Hitung Total ───
    public double hitungTotal() {
        return items.stream().mapToDouble(POItem::getSubtotal).sum();
    }

    // ─── Perilaku bisnis: Cek perlu approval? ───
    public boolean perluApproval() {
        return hitungTotal() > BATAS_APPROVAL;
    }

    // ─── Perilaku bisnis: Submit ───
    public void submit() {
        validasiDraft();
        if (items.isEmpty()) {
            throw new IllegalStateException("Tidak bisa submit PO kosong");
        }
        this.status = perluApproval() ? "MENUNGGU_APPROVAL" : "DISETUJUI";
    }

    // ─── Perilaku bisnis: Approve ───
    public void approve(String approver) {
        if (!"MENUNGGU_APPROVAL".equals(status)) {
            throw new IllegalStateException("PO tidak dalam status approval");
        }
        this.status = "DISETUJUI";
        System.out.println("  ✅ PO disetujui oleh: " + approver);
    }

    // ─── Helper ───
    private void validasiDraft() {
        if (!"DRAFT".equals(status)) {
            throw new IllegalStateException(
                "PO sudah di-submit, tidak bisa diubah");
        }
    }

    public String getStatus() { return status; }
    public String getNomorPO() { return nomorPO; }
    public List<POItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    // ─── Tampilkan ───
    public void cetak() {
        System.out.println("┌──────────────────────────────────────────┐");
        System.out.println("│  PURCHASE ORDER: " + nomorPO);
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  Vendor   : " + vendor);
        System.out.println("│  Tanggal  : " + tanggal);
        System.out.println("│  Status   : " + status);
        System.out.println("├──────────────────────────────────────────┤");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("│  " + (i + 1) + ". " + items.get(i));
        }
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  TOTAL: IDR " + String.format("%,.0f", hitungTotal()));
        if (perluApproval()) {
            System.out.println("│  ⚠️  Melebihi batas — perlu approval");
        }
        System.out.println("└──────────────────────────────────────────┘");
    }
}

// ═══════════════════════════════════════════
// APPLICATION SERVICE: PengadaanService
// (Orkestrator proses bisnis)
// ═══════════════════════════════════════════
class PengadaanService {
    public void prosesSubmit(PurchaseOrderBO po) {
        System.out.println("▸ Proses submit PO " + po.getNomorPO() + "...");
        po.submit();
        System.out.println("  Status setelah submit: " + po.getStatus());

        if (po.getStatus().equals("MENUNGGU_APPROVAL")) {
            System.out.println("  📋 PO masuk antrian approval manager.");
        } else {
            System.out.println("  🚀 PO langsung disetujui (di bawah batas).");
        }
    }
}

// ═══════════════════════════════════════════
// MAIN: Demonstrasi Business Object & Process
// ═══════════════════════════════════════════
public class PurchaseOrder {
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════════");
        System.out.println("  DEMO BUSINESS OBJECT & BUSINESS PROCESS");
        System.out.println("══════════════════════════════════════════\n");

        PengadaanService service = new PengadaanService();

        // ──── Skenario 1: PO kecil (auto-approve) ────
        System.out.println("▸ Skenario 1: PO kecil — auto approve\n");
        PurchaseOrderBO po1 = new PurchaseOrderBO("PO-2026-001", "PT Maju Terus");
        po1.tambahItem("Kertas A4", 50, 45_000);
        po1.tambahItem("Pulpen", 100, 5_000);
        po1.tambahItem("Tinta Printer", 10, 150_000);
        po1.cetak();
        System.out.println();
        service.prosesSubmit(po1);

        System.out.println("\n────────────────────────────────────\n");

        // ──── Skenario 2: PO besar (perlu approval) ────
        System.out.println("▸ Skenario 2: PO besar — perlu approval\n");
        PurchaseOrderBO po2 = new PurchaseOrderBO("PO-2026-002", "PT Tech Indonesia");
        po2.tambahItem("Laptop Kerja", 20, 12_000_000);
        po2.tambahItem("Monitor 24\"", 20, 3_500_000);
        po2.cetak();
        System.out.println();
        service.prosesSubmit(po2);
        System.out.println();
        po2.approve("Direktur Keuangan");
        System.out.println("  Status final: " + po2.getStatus());

        System.out.println("\n────────────────────────────────────\n");

        // ──── Skenario 3: Validasi bisnis ────
        System.out.println("▸ Skenario 3: Validasi aturan bisnis\n");

        // Coba item dengan harga negatif
        System.out.print("  Tambah item harga negatif: ");
        try {
            new POItem("Barang X", 1, -500);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // Coba ubah PO yang sudah di-submit
        System.out.print("  Ubah PO yang sudah submit: ");
        try {
            po1.tambahItem("Barang Baru", 1, 100_000);
        } catch (IllegalStateException e) {
            System.out.println("❌ " + e.getMessage());
        }

        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║  POIN PENTING:                            ║");
        System.out.println("║                                           ║");
        System.out.println("║  • PO menjaga aturan bisnisnya sendiri    ║");
        System.out.println("║  • Validasi ada DI DALAM objek            ║");
        System.out.println("║  • Service hanya orkestrator, bukan otak  ║");
        System.out.println("║  • Inilah Rich Domain Model               ║");
        System.out.println("╚═══════════════════════════════════════════╝");
    }
}
