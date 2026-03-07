/**
 * ═══════════════════════════════════════════════════════════
 *  BAB 9: WORKFLOW ENGINE — State Machine untuk Bisnis
 * ═══════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • State Machine: status + transisi
 *  • Guard: syarat sebelum transisi
 *  • Effect: aksi setelah transisi berhasil
 *  • Workflow engine yang deklaratif
 *
 *  Compile & Run:
 *    javac LeaveRequest.java
 *    java LeaveRequest
 * ═══════════════════════════════════════════════════════════
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════
// STATUS: Enum state
// ═══════════════════════════════════════════
enum StatusCuti {
    DRAFT, DIAJUKAN, DISETUJUI_ATASAN, DISETUJUI_HR, DITOLAK, DIBATALKAN
}

// ═══════════════════════════════════════════
// DOMAIN OBJECT: Pengajuan Cuti
// ═══════════════════════════════════════════
class CutiRequest {
    private final String id;
    private final String nama;
    private final LocalDate mulai;
    private final LocalDate selesai;
    private StatusCuti status;
    private int sisaCuti;
    private String alasanTolak;

    public CutiRequest(String id, String nama, LocalDate mulai,
                       LocalDate selesai, int sisaCuti) {
        this.id = id;
        this.nama = nama;
        this.mulai = mulai;
        this.selesai = selesai;
        this.sisaCuti = sisaCuti;
        this.status = StatusCuti.DRAFT;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public LocalDate getMulai() { return mulai; }
    public LocalDate getSelesai() { return selesai; }
    public StatusCuti getStatus() { return status; }
    public int getSisaCuti() { return sisaCuti; }
    public String getAlasanTolak() { return alasanTolak; }

    public void setStatus(StatusCuti status) { this.status = status; }
    public void setSisaCuti(int sisa) { this.sisaCuti = sisa; }
    public void setAlasanTolak(String alasan) { this.alasanTolak = alasan; }

    public long getJumlahHari() {
        return selesai.toEpochDay() - mulai.toEpochDay() + 1;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s s/d %s (%d hari) | Sisa: %d | Status: %s",
            id, nama, mulai, selesai, getJumlahHari(), sisaCuti, status);
    }
}

// ═══════════════════════════════════════════
// GUARD: Syarat sebelum transisi
// ═══════════════════════════════════════════
interface Guard {
    boolean boleh(CutiRequest req);
    String alasanGagal();
}

class CutiCukupGuard implements Guard {
    @Override
    public boolean boleh(CutiRequest req) {
        return req.getSisaCuti() >= req.getJumlahHari();
    }
    @Override
    public String alasanGagal() { return "Sisa cuti tidak mencukupi"; }
}

class TanggalValidGuard implements Guard {
    @Override
    public boolean boleh(CutiRequest req) {
        return !req.getSelesai().isBefore(req.getMulai());
    }
    @Override
    public String alasanGagal() { return "Tanggal selesai < tanggal mulai"; }
}

class MinimalH3Guard implements Guard {
    @Override
    public boolean boleh(CutiRequest req) {
        long hariBefore = req.getMulai().toEpochDay() - LocalDate.now().toEpochDay();
        return hariBefore >= 3;
    }
    @Override
    public String alasanGagal() { return "Pengajuan minimal H-3"; }
}

// ═══════════════════════════════════════════
// EFFECT: Aksi setelah transisi berhasil
// ═══════════════════════════════════════════
interface TransitionEffect {
    void execute(CutiRequest req);
}

class KurangiSisaCutiEffect implements TransitionEffect {
    @Override
    public void execute(CutiRequest req) {
        int sisa = (int)(req.getSisaCuti() - req.getJumlahHari());
        req.setSisaCuti(sisa);
        System.out.println("    💾 Sisa cuti dikurangi → " + sisa + " hari");
    }
}

class NotifAtasanEffect implements TransitionEffect {
    @Override
    public void execute(CutiRequest req) {
        System.out.println("    📧 Notifikasi ke atasan: Pengajuan cuti "
            + req.getNama() + " menunggu persetujuan.");
    }
}

class NotifHREffect implements TransitionEffect {
    @Override
    public void execute(CutiRequest req) {
        System.out.println("    📧 Notifikasi ke HR: Cuti " + req.getNama()
            + " sudah disetujui atasan.");
    }
}

class NotifKaryawanApprovedEffect implements TransitionEffect {
    @Override
    public void execute(CutiRequest req) {
        System.out.println("    📧 Notifikasi ke " + req.getNama()
            + ": Cuti Anda DISETUJUI.");
    }
}

class NotifKaryawanRejectedEffect implements TransitionEffect {
    @Override
    public void execute(CutiRequest req) {
        System.out.println("    📧 Notifikasi ke " + req.getNama()
            + ": Cuti Anda DITOLAK. Alasan: " + req.getAlasanTolak());
    }
}

// ═══════════════════════════════════════════
// TRANSITION: Definisi transisi status
// ═══════════════════════════════════════════
class Transition {
    private final String nama;
    private final StatusCuti dari;
    private final StatusCuti ke;
    private final List<Guard> guards;
    private final List<TransitionEffect> effects;

    public Transition(String nama, StatusCuti dari, StatusCuti ke,
                      List<Guard> guards, List<TransitionEffect> effects) {
        this.nama = nama;
        this.dari = dari;
        this.ke = ke;
        this.guards = guards;
        this.effects = effects;
    }

    public String getNama() { return nama; }
    public StatusCuti getDari() { return dari; }
    public StatusCuti getKe() { return ke; }
    public List<Guard> getGuards() { return guards; }
    public List<TransitionEffect> getEffects() { return effects; }
}

// ═══════════════════════════════════════════
// WORKFLOW ENGINE
// ═══════════════════════════════════════════
class CutiWorkflowEngine {
    private final List<Transition> transitions = new ArrayList<>();

    public void daftarTransisi(Transition t) {
        transitions.add(t);
    }

    public boolean jalankan(CutiRequest req, String namaTransisi) {
        System.out.println("\n  ▸ Transisi: " + namaTransisi);

        // Cari transisi
        Transition found = null;
        for (Transition t : transitions) {
            if (t.getNama().equals(namaTransisi) && t.getDari() == req.getStatus()) {
                found = t;
                break;
            }
        }

        if (found == null) {
            System.out.println("    ❌ Transisi '" + namaTransisi
                + "' tidak valid dari status " + req.getStatus());
            return false;
        }

        // Cek guards
        for (Guard g : found.getGuards()) {
            if (!g.boleh(req)) {
                System.out.println("    ❌ Guard gagal: " + g.alasanGagal());
                return false;
            }
        }

        // Ubah status
        StatusCuti lama = req.getStatus();
        req.setStatus(found.getKe());
        System.out.println("    ✅ " + lama + " → " + found.getKe());

        // Jalankan effects
        for (TransitionEffect e : found.getEffects()) {
            e.execute(req);
        }

        return true;
    }
}

// ═══════════════════════════════════════════
// MAIN: Demonstrasi Workflow Engine
// ═══════════════════════════════════════════
public class LeaveRequest {
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════════");
        System.out.println("  DEMO WORKFLOW ENGINE — Pengajuan Cuti");
        System.out.println("══════════════════════════════════════════");

        // ─── Setup Engine ───
        CutiWorkflowEngine engine = new CutiWorkflowEngine();

        // Transisi 1: DRAFT → DIAJUKAN
        engine.daftarTransisi(new Transition(
            "ajukan",
            StatusCuti.DRAFT,
            StatusCuti.DIAJUKAN,
            List.of(new CutiCukupGuard(), new TanggalValidGuard(),
                    new MinimalH3Guard()),
            List.of(new NotifAtasanEffect())
        ));

        // Transisi 2: DIAJUKAN → DISETUJUI_ATASAN
        engine.daftarTransisi(new Transition(
            "approve_atasan",
            StatusCuti.DIAJUKAN,
            StatusCuti.DISETUJUI_ATASAN,
            List.of(),
            List.of(new NotifHREffect())
        ));

        // Transisi 3: DISETUJUI_ATASAN → DISETUJUI_HR
        engine.daftarTransisi(new Transition(
            "approve_hr",
            StatusCuti.DISETUJUI_ATASAN,
            StatusCuti.DISETUJUI_HR,
            List.of(),
            List.of(new KurangiSisaCutiEffect(),
                    new NotifKaryawanApprovedEffect())
        ));

        // Transisi 4: DIAJUKAN → DITOLAK
        engine.daftarTransisi(new Transition(
            "tolak",
            StatusCuti.DIAJUKAN,
            StatusCuti.DITOLAK,
            List.of(),
            List.of(new NotifKaryawanRejectedEffect())
        ));

        // Transisi 5: DRAFT → DIBATALKAN
        engine.daftarTransisi(new Transition(
            "batalkan",
            StatusCuti.DRAFT,
            StatusCuti.DIBATALKAN,
            List.of(),
            List.of()
        ));

        // ─── Skenario 1: Happy path ───
        System.out.println("\n▸ SKENARIO 1: Cuti disetujui penuh\n");
        CutiRequest req1 = new CutiRequest(
            "CUTI-001", "Andi Wijaya",
            LocalDate.now().plusDays(7),
            LocalDate.now().plusDays(9),
            12
        );
        System.out.println("  " + req1);

        engine.jalankan(req1, "ajukan");
        engine.jalankan(req1, "approve_atasan");
        engine.jalankan(req1, "approve_hr");
        System.out.println("\n  Hasil: " + req1);

        // ─── Skenario 2: Ditolak ───
        System.out.println("\n────────────────────────────────────\n");
        System.out.println("▸ SKENARIO 2: Cuti ditolak atasan\n");
        CutiRequest req2 = new CutiRequest(
            "CUTI-002", "Siti Rahayu",
            LocalDate.now().plusDays(5),
            LocalDate.now().plusDays(6),
            12
        );
        System.out.println("  " + req2);

        engine.jalankan(req2, "ajukan");
        req2.setAlasanTolak("Deadline proyek minggu depan");
        engine.jalankan(req2, "tolak");
        System.out.println("\n  Hasil: " + req2);

        // ─── Skenario 3: Guard mencegah ───
        System.out.println("\n────────────────────────────────────\n");
        System.out.println("▸ SKENARIO 3: Sisa cuti tidak cukup\n");
        CutiRequest req3 = new CutiRequest(
            "CUTI-003", "Budi Santoso",
            LocalDate.now().plusDays(5),
            LocalDate.now().plusDays(15),
            5
        );
        System.out.println("  " + req3);
        System.out.println("  Minta cuti " + req3.getJumlahHari()
            + " hari, sisa " + req3.getSisaCuti() + " hari");
        engine.jalankan(req3, "ajukan");
        System.out.println("\n  Hasil: " + req3);

        // ─── Skenario 4: Transisi tidak valid ───
        System.out.println("\n────────────────────────────────────\n");
        System.out.println("▸ SKENARIO 4: Transisi tidak valid\n");
        System.out.println("  Coba approve cuti yang sudah ditolak:");
        engine.jalankan(req2, "approve_atasan");

        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  POIN PENTING:");
        System.out.println("  • State machine: status + transisi yang jelas");
        System.out.println("  • Guard: validasi SEBELUM transisi");
        System.out.println("  • Effect: aksi SETELAH transisi berhasil");
        System.out.println("  • Engine deklaratif, mudah ditambah transisi");
        System.out.println("══════════════════════════════════════════");
    }
}
