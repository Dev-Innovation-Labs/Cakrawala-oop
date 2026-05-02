package id.universitas.cakrawala.service;

import id.universitas.cakrawala.domain.PurchaseOrderBO;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * APPLICATION SERVICE: PengadaanService
 * Orkestrator proses bisnis Purchase Order
 */
@Service
public class PengadaanService {
    private Map<String, PurchaseOrderBO> poRepository = new HashMap<>();
    private int poCounter = 0;

    /**
     * Buat PO baru
     */
    public PurchaseOrderBO buatPO(String vendor) {
        poCounter++;
        String nomorPO = "PO-" + System.currentTimeMillis() + "-" + poCounter;
        PurchaseOrderBO po = new PurchaseOrderBO(nomorPO, vendor);
        poRepository.put(nomorPO, po);
        return po;
    }

    /**
     * Ambil PO berdasarkan nomor
     */
    public PurchaseOrderBO getPO(String nomorPO) {
        PurchaseOrderBO po = poRepository.get(nomorPO);
        if (po == null) {
            throw new IllegalArgumentException("PO tidak ditemukan: " + nomorPO);
        }
        return po;
    }

    /**
     * Tambah item ke PO
     */
    public void tambahItem(String nomorPO, String namaBarang, int jumlah, double harga) {
        PurchaseOrderBO po = getPO(nomorPO);
        po.tambahItem(namaBarang, jumlah, harga);
    }

    /**
     * Submit PO
     */
    public void submitPO(String nomorPO) {
        PurchaseOrderBO po = getPO(nomorPO);
        po.submit();
    }

    /**
     * Approve PO
     */
    public void approvePO(String nomorPO, String approver) {
        PurchaseOrderBO po = getPO(nomorPO);
        po.approve(approver);
    }

    /**
     * Hapus item dari PO
     */
    public void hapusItem(String nomorPO, int index) {
        PurchaseOrderBO po = getPO(nomorPO);
        po.hapusItem(index);
    }

    /**
     * Daftar vendor contoh
     */
    public String[] getDaftarVendor() {
        return new String[]{
            "PT Maju Terus",
            "CV Sukses Bersama",
            "UD Perkembangan",
            "PT Teknologi Maju",
            "Supplier Premium"
        };
    }

    /**
     * Daftar barang contoh
     */
    public String[] getDaftarBarang() {
        return new String[]{
            "Kertas A4",
            "Pulpen Ballpoint",
            "Tinta Printer",
            "Laptop",
            "Monitor",
            "Keyboard",
            "Mouse",
            "Printer",
            "Scanner",
            "Headphone"
        };
    }
}
