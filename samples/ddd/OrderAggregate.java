/**
 * ═══════════════════════════════════════════════════════════
 *  BAB 6: DOMAIN DRIVEN DESIGN — Entity, Value Object, Aggregate
 * ═══════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Entity (Pesanan — identitas unik)
 *  • Value Object (Uang — immutable, tanpa identitas)
 *  • Aggregate Root (PesananAggregate — penjaga konsistensi)
 *  • Repository (penyimpanan & pengambilan aggregate)
 *
 *  Compile & Run:
 *    javac OrderAggregate.java
 *    java OrderAggregate
 * ═══════════════════════════════════════════════════════════
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// ═══════════════════════════════════════════
// VALUE OBJECT: Uang (Immutable)
// ═══════════════════════════════════════════
class Uang {
    private final double jumlah;
    private final String mataUang;

    public Uang(double jumlah, String mataUang) {
        if (jumlah < 0) throw new IllegalArgumentException("Jumlah negatif");
        this.jumlah = jumlah;
        this.mataUang = mataUang;
    }

    public Uang tambah(Uang lain) {
        if (!this.mataUang.equals(lain.mataUang))
            throw new IllegalArgumentException("Mata uang berbeda");
        return new Uang(this.jumlah + lain.jumlah, this.mataUang);
    }

    public double getJumlah() { return jumlah; }
    public String getMataUang() { return mataUang; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Uang) {
            Uang o = (Uang) obj;
            return this.jumlah == o.jumlah && this.mataUang.equals(o.mataUang);
        }
        return false;
    }

    @Override
    public String toString() {
        return mataUang + " " + String.format("%,.0f", jumlah);
    }
}

// ═══════════════════════════════════════════
// VALUE OBJECT: Alamat (Immutable)
// ═══════════════════════════════════════════
class Alamat {
    private final String jalan;
    private final String kota;
    private final String kodePos;

    public Alamat(String jalan, String kota, String kodePos) {
        this.jalan = jalan;
        this.kota = kota;
        this.kodePos = kodePos;
    }

    @Override
    public String toString() {
        return jalan + ", " + kota + " " + kodePos;
    }
}

// ═══════════════════════════════════════════
// ENTITY: ItemPesanan (di dalam aggregate)
// ═══════════════════════════════════════════
class ItemPesanan {
    private final String namaProduk;
    private final int jumlah;
    private final Uang hargaSatuan;

    public ItemPesanan(String namaProduk, int jumlah, Uang hargaSatuan) {
        if (jumlah <= 0) throw new IllegalArgumentException("Jumlah harus > 0");
        this.namaProduk = namaProduk;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
    }

    public Uang getSubtotal() {
        return new Uang(jumlah * hargaSatuan.getJumlah(), hargaSatuan.getMataUang());
    }

    public String getNamaProduk() { return namaProduk; }
    public int getJumlah() { return jumlah; }

    @Override
    public String toString() {
        return String.format("%-18s x%d @ %s = %s",
            namaProduk, jumlah, hargaSatuan, getSubtotal());
    }
}

// ═══════════════════════════════════════════
// AGGREGATE ROOT: Pesanan
// ═══════════════════════════════════════════
class PesananAggregate {
    private final String nomorPesanan;
    private final String namaPelanggan;
    private final Alamat alamatKirim;
    private final List<ItemPesanan> items = new ArrayList<>();
    private String status;

    public PesananAggregate(String nomorPesanan, String namaPelanggan,
                            Alamat alamatKirim) {
        this.nomorPesanan = nomorPesanan;
        this.namaPelanggan = namaPelanggan;
        this.alamatKirim = alamatKirim;
        this.status = "BARU";
    }

    // Bisnis: Tambah item lewat ROOT
    public void tambahItem(String produk, int jumlah, Uang harga) {
        if (!"BARU".equals(status)) {
            throw new IllegalStateException(
                "Tidak bisa tambah item — status: " + status);
        }
        items.add(new ItemPesanan(produk, jumlah, harga));
    }

    // Bisnis: Hitung total
    public Uang hitungTotal() {
        Uang total = new Uang(0, "IDR");
        for (ItemPesanan item : items) {
            total = total.tambah(item.getSubtotal());
        }
        return total;
    }

    // Bisnis: Konfirmasi pesanan
    public void konfirmasi() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Pesanan kosong!");
        }
        if (!"BARU".equals(status)) {
            throw new IllegalStateException("Sudah dikonfirmasi");
        }
        this.status = "DIKONFIRMASI";
    }

    // Read-only access
    public String getNomorPesanan() { return nomorPesanan; }
    public String getStatus() { return status; }
    public List<ItemPesanan> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void tampilkan() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║  PESANAN: " + nomorPesanan);
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║  Pelanggan : " + namaPelanggan);
        System.out.println("║  Alamat    : " + alamatKirim);
        System.out.println("║  Status    : " + status);
        System.out.println("╠═══════════════════════════════════════╣");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("║  " + (i + 1) + ". " + items.get(i));
        }
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║  TOTAL: " + hitungTotal());
        System.out.println("╚═══════════════════════════════════════╝");
    }
}

// ═══════════════════════════════════════════
// REPOSITORY: Simpan & Ambil Aggregate
// ═══════════════════════════════════════════
interface PesananRepository {
    void simpan(PesananAggregate pesanan);
    Optional<PesananAggregate> cariByNomor(String nomor);
}

class PesananRepositoryMemory implements PesananRepository {
    private Map<String, PesananAggregate> storage = new HashMap<>();

    @Override
    public void simpan(PesananAggregate pesanan) {
        storage.put(pesanan.getNomorPesanan(), pesanan);
        System.out.println("💾 Pesanan " + pesanan.getNomorPesanan() + " tersimpan.");
    }

    @Override
    public Optional<PesananAggregate> cariByNomor(String nomor) {
        return Optional.ofNullable(storage.get(nomor));
    }
}

// ═══════════════════════════════════════════
// MAIN: Demonstrasi DDD
// ═══════════════════════════════════════════
public class OrderAggregate {
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════════");
        System.out.println("  DEMO DDD — Entity, Value Object, Aggregate");
        System.out.println("══════════════════════════════════════════\n");

        // Value Objects
        Alamat alamat = new Alamat("Jl. Sudirman No. 1", "Jakarta", "10220");
        Uang hargaLaptop = new Uang(15_000_000, "IDR");
        Uang hargaMouse = new Uang(250_000, "IDR");

        System.out.println("▸ Value Object: Uang");
        System.out.println("  " + hargaLaptop);
        Uang total = hargaLaptop.tambah(hargaMouse);
        System.out.println("  " + hargaLaptop + " + " + hargaMouse + " = " + total);

        System.out.println("\n▸ Aggregate Root: Pesanan\n");

        // Buat Aggregate
        PesananAggregate pesanan = new PesananAggregate(
            "ORD-2026-001", "Budi Santoso", alamat);

        // Tambah items lewat ROOT
        pesanan.tambahItem("Laptop Gaming", 1, hargaLaptop);
        pesanan.tambahItem("Mouse Wireless", 2, hargaMouse);
        pesanan.tampilkan();

        // Konfirmasi
        System.out.println("\n▸ Konfirmasi Pesanan");
        pesanan.konfirmasi();
        System.out.println("  Status: " + pesanan.getStatus());

        // Coba tambah item setelah konfirmasi
        System.out.println("\n▸ Coba tambah item setelah konfirmasi:");
        try {
            pesanan.tambahItem("Keyboard", 1, new Uang(500000, "IDR"));
        } catch (IllegalStateException e) {
            System.out.println("  ❌ " + e.getMessage());
        }

        // Repository
        System.out.println("\n▸ Repository: Simpan & Cari");
        PesananRepository repo = new PesananRepositoryMemory();
        repo.simpan(pesanan);

        Optional<PesananAggregate> found = repo.cariByNomor("ORD-2026-001");
        found.ifPresent(p -> System.out.println("  Ditemukan: " + p.getNomorPesanan()
            + " [" + p.getStatus() + "]"));

        System.out.println("\n══════════════════════════════════════════");
    }
}
