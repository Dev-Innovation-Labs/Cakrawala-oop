package id.universitas.cakrawala.workflow.guards;

import id.universitas.cakrawala.domain.LeaveRequest;
import id.universitas.cakrawala.workflow.Guard;
import java.time.LocalDate;

/**
 * Guard: Validasi pengajuan minimal H-3 (3 hari sebelumnya)
 */
public class MinimalH3Guard implements Guard {
    @Override
    public boolean boleh(LeaveRequest req) {
        long hariBefore = req.getMulai().toEpochDay() - LocalDate.now().toEpochDay();
        return hariBefore >= 3;
    }

    @Override
    public String alasanGagal() {
        return "Pengajuan cuti harus minimal H-3 (3 hari sebelumnya)";
    }
}
