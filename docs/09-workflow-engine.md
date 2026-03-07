# ⚙️ Bab 9: Workflow Engine

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Business Object](08-business-object-process.md) · [⬅️ Beranda](../README.md) · [Modular Monolith ➡️](10-modular-monolith.md)

---

## 📌 Apa Itu Workflow Engine?

**Workflow Engine** adalah mesin yang mengelola **alur kerja (workflow)** secara otomatis — menentukan status apa yang valid, transisi mana yang diperbolehkan, siapa yang boleh melakukan aksi, dan apa yang terjadi saat transisi dilakukan.

> **Inti dari Workflow Engine:** _"Alih-alih menulis if-else untuk setiap kemungkinan status, definisikan aturannya sekali, dan biarkan engine yang menjalankan."_

---

## 🧠 Analogi Dunia Nyata

### 🏧 Mesin ATM

Mesin ATM adalah contoh sempurna workflow engine:

```
   ┌────────────┐  masukkan kartu  ┌──────────────┐
   │   IDLE     │─────────────────►│ BACA KARTU   │
   │ (menunggu) │                  │              │
   └────────────┘                  └──────┬───────┘
                                          │ kartu valid
                                          ▼
   ┌────────────┐  PIN salah 3x   ┌──────────────┐
   │KARTU DITAHAN│◄───────────────│ INPUT PIN    │
   └────────────┘                  └──────┬───────┘
                                          │ PIN benar
                                          ▼
                                   ┌──────────────┐
                                   │ PILIH MENU   │
                                   └──────┬───────┘
                                    ┌─────┴─────┐
                                    ▼           ▼
                              ┌──────────┐ ┌──────────┐
                              │TARIK TUNAI│ │CEK SALDO │
                              └────┬─────┘ └────┬─────┘
                                   │            │
                                   ▼            ▼
                              ┌──────────────────────┐
                              │    CETAK STRUK        │
                              └──────────┬───────────┘
                                         │
                                         ▼
                              ┌──────────────────────┐
                              │  KELUARKAN KARTU     │
                              │  → Kembali ke IDLE   │
                              └──────────────────────┘
```

Setiap **kotak** = Status (State)  
Setiap **panah** = Transisi (Transition)  
Setiap **label** = Aksi/kondisi (Guard)

---

## 🔑 Komponen Workflow Engine

```
┌─────────────────────────────────────────────────────┐
│              WORKFLOW ENGINE                          │
├──────────────┬──────────────────────────────────────┤
│  STATE       │  Kondisi/status saat ini              │
│              │  ("DRAFT", "MENUNGGU", "DISETUJUI")   │
├──────────────┼──────────────────────────────────────┤
│  TRANSITION  │  Perpindahan dari satu state ke state │
│              │  lain (DRAFT → MENUNGGU via "ajukan") │
├──────────────┼──────────────────────────────────────┤
│  ACTION      │  Aksi yang memicu transisi            │
│              │  ("ajukan", "setujui", "tolak")       │
├──────────────┼──────────────────────────────────────┤
│  GUARD       │  Syarat yang harus dipenuhi sebelum   │
│              │  transisi boleh terjadi                │
├──────────────┼──────────────────────────────────────┤
│  EFFECT      │  Efek samping saat transisi terjadi   │
│              │  (kirim email, catat log)              │
└──────────────┴──────────────────────────────────────┘
```

---

## 💻 Contoh: Workflow Pengajuan Cuti

### Skenario Bisnis

```
    ┌───────┐  ajukan   ┌───────────┐  setujui_mgr  ┌───────────┐
    │ DRAFT │──────────►│ DIAJUKAN  │──────────────►│DISETUJUI  │
    │       │           │           │                │MANAGER    │
    └───────┘           └─────┬─────┘                └─────┬─────┘
        │                     │                            │
        │ batal               │ tolak                      │ setujui_hr
        ▼                     ▼                            ▼
    ┌───────┐           ┌───────────┐              ┌───────────┐
    │DIBATAL│           │  DITOLAK  │              │DISETUJUI  │
    │KAN    │           │           │              │HR (Final) │
    └───────┘           └───────────┘              └───────────┘

    Guard: cuti > 3 hari → harus approval HR setelah Manager
    Guard: cuti ≤ 3 hari → cukup approval Manager saja
```

### Implementasi

```java
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ═══════════════════════════════════════
 * WORKFLOW STATE: Enum status cuti
 * ═══════════════════════════════════════
 */
enum StatusCuti {
    DRAFT,
    DIAJUKAN,
    DISETUJUI_MANAGER,
    DISETUJUI_HR,
    DITOLAK,
    DIBATALKAN
}

/**
 * ═══════════════════════════════════════
 * TRANSITION: Definisi perpindahan status
 * ═══════════════════════════════════════
 */
class Transition {
    private final StatusCuti dari;
    private final StatusCuti ke;
    private final String aksi;

    public Transition(StatusCuti dari, String aksi, StatusCuti ke) {
        this.dari = dari;
        this.aksi = aksi;
        this.ke = ke;
    }

    public StatusCuti getDari() { return dari; }
    public StatusCuti getKe() { return ke; }
    public String getAksi() { return aksi; }
}

/**
 * ═══════════════════════════════════════
 * GUARD: Syarat untuk transisi
 * ═══════════════════════════════════════
 */
interface Guard {
    boolean bolehTransisi(CutiRequest cuti);
    String getAlasan();
}

// Guard: Cuti harus punya alasan
class HarusAdaAlasan implements Guard {
    @Override
    public boolean bolehTransisi(CutiRequest cuti) {
        return cuti.getAlasan() != null && !cuti.getAlasan().trim().isEmpty();
    }

    @Override
    public String getAlasan() {
        return "Alasan cuti wajib diisi";
    }
}

// Guard: Sisa cuti harus cukup
class SisaCutiCukup implements Guard {
    @Override
    public boolean bolehTransisi(CutiRequest cuti) {
        return cuti.getJumlahHari() <= cuti.getSisaCuti();
    }

    @Override
    public String getAlasan() {
        return "Sisa cuti tidak mencukupi";
    }
}

/**
 * ═══════════════════════════════════════
 * EFFECT: Efek samping saat transisi
 * ═══════════════════════════════════════
 */
interface TransitionEffect {
    void execute(CutiRequest cuti, String aktor);
}

class LogEffect implements TransitionEffect {
    @Override
    public void execute(CutiRequest cuti, String aktor) {
        System.out.println("📋 Log: Cuti " + cuti.getNomor()
            + " → " + cuti.getStatus() + " oleh " + aktor);
    }
}

class NotifEffect implements TransitionEffect {
    @Override
    public void execute(CutiRequest cuti, String aktor) {
        System.out.println("📧 Notif: " + cuti.getPemohon()
            + " — status cuti Anda: " + cuti.getStatus());
    }
}

/**
 * ═══════════════════════════════════════
 * BUSINESS OBJECT: Pengajuan Cuti
 * ═══════════════════════════════════════
 */
class CutiRequest {
    private final String nomor;
    private final String pemohon;
    private final LocalDate tanggalMulai;
    private final int jumlahHari;
    private final String alasan;
    private final int sisaCuti;
    private StatusCuti status;
    private List<String> riwayat;

    public CutiRequest(String nomor, String pemohon,
                       LocalDate tanggalMulai, int jumlahHari,
                       String alasan, int sisaCuti) {
        this.nomor = nomor;
        this.pemohon = pemohon;
        this.tanggalMulai = tanggalMulai;
        this.jumlahHari = jumlahHari;
        this.alasan = alasan;
        this.sisaCuti = sisaCuti;
        this.status = StatusCuti.DRAFT;
        this.riwayat = new ArrayList<>();
        riwayat.add("Dibuat pada " + LocalDate.now());
    }

    public void setStatus(StatusCuti status) {
        this.status = status;
    }

    public void tambahRiwayat(String catatan) {
        riwayat.add(catatan);
    }

    // Getters
    public String getNomor() { return nomor; }
    public String getPemohon() { return pemohon; }
    public int getJumlahHari() { return jumlahHari; }
    public String getAlasan() { return alasan; }
    public int getSisaCuti() { return sisaCuti; }
    public StatusCuti getStatus() { return status; }
    public boolean perluApprovalHR() { return jumlahHari > 3; }

    public void tampilkan() {
        System.out.println("╔═══════════════════════════════════╗");
        System.out.println("║  PENGAJUAN CUTI: " + nomor);
        System.out.println("╠═══════════════════════════════════╣");
        System.out.println("║  Pemohon     : " + pemohon);
        System.out.println("║  Mulai       : " + tanggalMulai);
        System.out.println("║  Jumlah Hari : " + jumlahHari);
        System.out.println("║  Alasan      : " + alasan);
        System.out.println("║  Sisa Cuti   : " + sisaCuti + " hari");
        System.out.println("║  Status      : " + status);
        System.out.println("╠═══════════════════════════════════╣");
        System.out.println("║  Riwayat:");
        for (String r : riwayat) {
            System.out.println("║    • " + r);
        }
        System.out.println("╚═══════════════════════════════════╝");
    }
}

/**
 * ═══════════════════════════════════════
 * WORKFLOW ENGINE: Mesin alur kerja cuti
 *
 * Engine ini TIDAK hardcode logic —
 * ia berjalan berdasarkan DEFINISI
 * transition, guard, dan effect.
 * ═══════════════════════════════════════
 */
class CutiWorkflowEngine {

    private List<Transition> transitions = new ArrayList<>();
    private List<Guard> guards = new ArrayList<>();
    private List<TransitionEffect> effects = new ArrayList<>();

    public CutiWorkflowEngine() {
        // Definisikan semua transisi yang valid
        transitions.add(new Transition(StatusCuti.DRAFT,
            "ajukan", StatusCuti.DIAJUKAN));
        transitions.add(new Transition(StatusCuti.DRAFT,
            "batalkan", StatusCuti.DIBATALKAN));
        transitions.add(new Transition(StatusCuti.DIAJUKAN,
            "setujui_manager", StatusCuti.DISETUJUI_MANAGER));
        transitions.add(new Transition(StatusCuti.DIAJUKAN,
            "tolak", StatusCuti.DITOLAK));
        transitions.add(new Transition(StatusCuti.DISETUJUI_MANAGER,
            "setujui_hr", StatusCuti.DISETUJUI_HR));
        transitions.add(new Transition(StatusCuti.DISETUJUI_MANAGER,
            "tolak", StatusCuti.DITOLAK));

        // Definisikan guard (syarat)
        guards.add(new HarusAdaAlasan());
        guards.add(new SisaCutiCukup());

        // Definisikan effect (efek samping)
        effects.add(new LogEffect());
        effects.add(new NotifEffect());
    }

    /**
     * Jalankan aksi pada cuti request
     */
    public void jalankan(CutiRequest cuti, String aksi, String aktor) {
        System.out.println("\n▸ Aksi: " + aksi.toUpperCase()
            + " oleh " + aktor);

        // 1. Cari transisi yang cocok
        Transition t = cariTransisi(cuti.getStatus(), aksi);
        if (t == null) {
            System.out.println("  ❌ Aksi '" + aksi
                + "' tidak valid untuk status " + cuti.getStatus());
            return;
        }

        // 2. Cek semua guard (khusus saat ajukan)
        if ("ajukan".equals(aksi)) {
            for (Guard guard : guards) {
                if (!guard.bolehTransisi(cuti)) {
                    System.out.println("  ❌ Guard gagal: "
                        + guard.getAlasan());
                    return;
                }
            }
        }

        // 3. Jalankan transisi
        StatusCuti statusLama = cuti.getStatus();
        cuti.setStatus(t.getKe());
        cuti.tambahRiwayat(aksi + " oleh " + aktor
            + " (" + statusLama + " → " + t.getKe() + ")");

        System.out.println("  ✅ " + statusLama + " → " + t.getKe());

        // 4. Jalankan effects
        for (TransitionEffect effect : effects) {
            effect.execute(cuti, aktor);
        }

        // 5. Cek apakah perlu approval tambahan
        if (t.getKe() == StatusCuti.DISETUJUI_MANAGER
            && cuti.perluApprovalHR()) {
            System.out.println("  ⚠️ Cuti > 3 hari → masih perlu approval HR");
        }
    }

    private Transition cariTransisi(StatusCuti statusSaatIni, String aksi) {
        for (Transition t : transitions) {
            if (t.getDari() == statusSaatIni
                && t.getAksi().equals(aksi)) {
                return t;
            }
        }
        return null;
    }
}
```

---

## ▶️ Demo Workflow

```java
public class LeaveRequest {
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════");
        System.out.println("   DEMO WORKFLOW ENGINE — CUTI        ");
        System.out.println("══════════════════════════════════════");

        CutiWorkflowEngine engine = new CutiWorkflowEngine();

        // Buat pengajuan cuti 5 hari (perlu approval HR)
        CutiRequest cuti = new CutiRequest(
            "CUTI-001", "Budi Santoso",
            LocalDate.of(2026, 3, 10), 5,
            "Liburan keluarga", 12
        );

        cuti.tampilkan();

        // Jalankan workflow step by step
        engine.jalankan(cuti, "ajukan", "Budi Santoso");
        engine.jalankan(cuti, "setujui_manager", "Pak Direktur");
        engine.jalankan(cuti, "setujui_hr", "Ibu HRD");

        System.out.println();
        cuti.tampilkan();

        System.out.println("\n── Coba aksi tidak valid: ──");
        engine.jalankan(cuti, "ajukan", "Budi");  // Sudah final!

        System.out.println("\n══════════════════════════════════════");
    }
}
```

---

## 🔍 Keuntungan Workflow Engine

| Tanpa Engine | Dengan Engine |
|:-------------|:-------------|
| If-else bertingkat di mana-mana | Aturan didefinisikan secara deklaratif |
| Sulit tahu transisi mana yang valid | Transisi eksplisit dan terdokumentasi |
| Menambah status = ubah banyak tempat | Menambah status = tambah Transition baru |
| Guard/validasi tersebar | Guard terpusat dan bisa di-reuse |
| Efek samping sulit dilacak | Effect didefinisikan jelas per transisi |

---

## 📋 Checklist Workflow Engine

- [ ] Semua **state** didefinisikan eksplisit (enum/class)
- [ ] Semua **transisi** didefinisikan secara deklaratif
- [ ] **Guard** memvalidasi syarat sebelum transisi
- [ ] **Effect** menangani efek samping setelah transisi
- [ ] Aksi yang tidak valid ditolak dengan pesan jelas
- [ ] Riwayat transisi dicatat untuk audit trail

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📖 ← Business Object](08-business-object-process.md) | [📖 Modular Monolith →](10-modular-monolith.md) |

---

<p align="center"><i>"Workflow Engine mengubah alur kerja dari kode spaghetti menjadi diagram yang bisa dibaca, diuji, dan diaudit."</i></p>

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
