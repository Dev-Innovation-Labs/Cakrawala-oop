package id.universitas.cakrawala.workflow;

import id.universitas.cakrawala.domain.LeaveRequest;
import id.universitas.cakrawala.domain.LeaveStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════
 *  WORKFLOW ENGINE: State Machine untuk Bisnis
 * ═══════════════════════════════════════════════════════════
 *
 *  Mengelola:
 *  • Status (state)
 *  • Transisi antar status
 *  • Guard: syarat sebelum transisi
 *  • Effect: aksi setelah transisi berhasil
 */
public class LeaveWorkflowEngine {
    private final List<Transition> transitions = new ArrayList<>();

    /**
     * Daftarkan transisi ke dalam engine
     */
    public void daftarTransisi(Transition t) {
        transitions.add(t);
    }

    /**
     * Jalankan transisi untuk perubahan status
     * @return true jika transisi berhasil, false jika gagal
     */
    public TransitionResult jalankan(LeaveRequest req, String namaTransisi) {
        // Cari transisi
        Transition found = null;
        for (Transition t : transitions) {
            if (t.getNama().equals(namaTransisi) && t.getDari() == req.getStatus()) {
                found = t;
                break;
            }
        }

        if (found == null) {
            String pesan = String.format(
                "Transisi '%s' tidak valid dari status %s",
                namaTransisi, req.getStatus().getLabel()
            );
            return new TransitionResult(false, pesan, null);
        }

        // Cek semua guards
        for (Guard g : found.getGuards()) {
            if (!g.boleh(req)) {
                return new TransitionResult(false, "Guard gagal: " + g.alasanGagal(), null);
            }
        }

        // Ubah status
        LeaveStatus statusLama = req.getStatus();
        req.setStatus(found.getKe());

        // Jalankan semua effects
        List<String> effects = new ArrayList<>();
        for (TransitionEffect e : found.getEffects()) {
            e.execute(req);
            effects.add(e.getClass().getSimpleName());
        }

        String pesan = String.format("%s → %s", statusLama.getLabel(), found.getKe().getLabel());
        return new TransitionResult(true, pesan, effects);
    }

    /**
     * Inner class untuk hasil transisi
     */
    public static class TransitionResult {
        private final boolean sukses;
        private final String pesan;
        private final List<String> efekYangDijalankan;

        public TransitionResult(boolean sukses, String pesan, List<String> efekYangDijalankan) {
            this.sukses = sukses;
            this.pesan = pesan;
            this.efekYangDijalankan = efekYangDijalankan;
        }

        public boolean isSukses() { return sukses; }
        public String getPesan() { return pesan; }
        public List<String> getEfekYangDijalankan() { return efekYangDijalankan; }
    }
}
