package id.universitas.cakrawala.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BUSINESS OBJECT: PurchaseOrderBO (Rich Domain Model)
 * Mengandung:
 * - Data (state)
 * - Perilaku bisnis (business rules)
 * - Validasi internal
 */
public class PurchaseOrderBO {
    
    // --- Identitas dan metadata
    private final String nomorPO;
    private final String vendor;
    private final LocalDate tanggal;

    // --- State
    private final List<POItem> items = new ArrayList<>();
    private String status;
    private String approver;

    // --- Aturan Bisnis (Business Rules)
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
    public void approve(String approverName) {
        if (!"MENUNGGU_APPROVAL".equals(status)) {
            throw new IllegalStateException("PO tidak dalam status menunggu approval");
        }
        this.status = "DISETUJUI";
        this.approver = approverName;
    }

    // ─── Helper ───
    private void validasiDraft() {
        if (!"DRAFT".equals(status)) {
            throw new IllegalStateException(
                "PO sudah di-submit, tidak bisa diubah");
        }
    }

    public void hapusItem(int index) {
        validasiDraft();
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public void reset() {
        if (!status.equals("DRAFT")) {
            throw new IllegalStateException("Hanya PO draft yang bisa direset");
        }
        items.clear();
    }

    // --- Getters ---
    public String getNomorPO() { return nomorPO; }
    public String getVendor() { return vendor; }
    public LocalDate getTanggal() { return tanggal; }
    public String getStatus() { return status; }
    public String getApprover() { return approver; }
    public List<POItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    public double getBatasApproval() { return BATAS_APPROVAL; }
}
