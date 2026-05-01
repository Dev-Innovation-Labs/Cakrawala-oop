package id.universitas.cakrawala.workflow.guards;

import id.universitas.cakrawala.domain.LeaveRequest;
import id.universitas.cakrawala.workflow.Guard;
import java.time.LocalDate;

/**
 * Guard: Validasi tanggal selesai >= tanggal mulai
 */
public class TanggalValidGuard implements Guard {
    @Override
    public boolean boleh(LeaveRequest req) {
        return !req.getSelesai().isBefore(req.getMulai());
    }

    @Override
    public String alasanGagal() {
        return "Tanggal selesai tidak boleh sebelum tanggal mulai";
    }
}
