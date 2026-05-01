package id.universitas.cakrawala.domain;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Domain Model: Pengajuan Cuti
 */
public class LeaveRequest {
    private final String id;
    private final String nama;
    private final String email;
    private final LocalDate mulai;
    private final LocalDate selesai;
    private LeaveStatus status;
    private int sisaCuti;
    private String alasanTolak;
    private LocalDate dibuat;

    public LeaveRequest(String nama, String email, LocalDate mulai,
                       LocalDate selesai, int sisaCuti) {
        this.id = UUID.randomUUID().toString();
        this.nama = nama;
        this.email = email;
        this.mulai = mulai;
        this.selesai = selesai;
        this.sisaCuti = sisaCuti;
        this.status = LeaveStatus.DRAFT;
        this.dibuat = LocalDate.now();
    }

    // Getters
    public String getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public LocalDate getMulai() { return mulai; }
    public LocalDate getSelesai() { return selesai; }
    public LeaveStatus getStatus() { return status; }
    public int getSisaCuti() { return sisaCuti; }
    public String getAlasanTolak() { return alasanTolak; }
    public LocalDate getDibuat() { return dibuat; }

    // Setters
    public void setStatus(LeaveStatus status) { this.status = status; }
    public void setSisaCuti(int sisa) { this.sisaCuti = sisa; }
    public void setAlasanTolak(String alasan) { this.alasanTolak = alasan; }

    public long getJumlahHari() {
        return selesai.toEpochDay() - mulai.toEpochDay() + 1;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s s/d %s (%d hari) | Sisa: %d | Status: %s",
            id, nama, mulai, selesai, getJumlahHari(), sisaCuti, status.getLabel());
    }
}
