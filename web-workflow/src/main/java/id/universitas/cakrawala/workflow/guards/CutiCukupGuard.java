package id.universitas.cakrawala.workflow.guards;

import id.universitas.cakrawala.domain.LeaveRequest;
import id.universitas.cakrawala.workflow.Guard;
import java.time.LocalDate;

/**
 * Guard: Validasi sisa cuti cukup
 */
public class CutiCukupGuard implements Guard {
    @Override
    public boolean boleh(LeaveRequest req) {
        return req.getSisaCuti() >= req.getJumlahHari();
    }

    @Override
    public String alasanGagal() {
        return "Sisa cuti tidak mencukupi";
    }
}
