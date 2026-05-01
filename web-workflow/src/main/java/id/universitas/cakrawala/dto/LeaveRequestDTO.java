package id.universitas.cakrawala.dto;

import id.universitas.cakrawala.domain.LeaveRequest;
import id.universitas.cakrawala.domain.LeaveStatus;
import java.time.LocalDate;

/**
 * DTO untuk Leave Request
 */
public class LeaveRequestDTO {
    private String id;
    private String nama;
    private String email;
    private LocalDate mulai;
    private LocalDate selesai;
    private String status;
    private String statusLabel;
    private String statusColor;
    private int sisaCuti;
    private long jumlahHari;
    private String alasanTolak;
    private LocalDate dibuat;

    public LeaveRequestDTO() {}

    public LeaveRequestDTO(LeaveRequest req) {
        this.id = req.getId();
        this.nama = req.getNama();
        this.email = req.getEmail();
        this.mulai = req.getMulai();
        this.selesai = req.getSelesai();
        this.status = req.getStatus().name();
        this.statusLabel = req.getStatus().getLabel();
        this.statusColor = req.getStatus().getColor();
        this.sisaCuti = req.getSisaCuti();
        this.jumlahHari = req.getJumlahHari();
        this.alasanTolak = req.getAlasanTolak();
        this.dibuat = req.getDibuat();
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getMulai() { return mulai; }
    public void setMulai(LocalDate mulai) { this.mulai = mulai; }

    public LocalDate getSelesai() { return selesai; }
    public void setSelesai(LocalDate selesai) { this.selesai = selesai; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }

    public String getStatusColor() { return statusColor; }
    public void setStatusColor(String statusColor) { this.statusColor = statusColor; }

    public int getSisaCuti() { return sisaCuti; }
    public void setSisaCuti(int sisaCuti) { this.sisaCuti = sisaCuti; }

    public long getJumlahHari() { return jumlahHari; }
    public void setJumlahHari(long jumlahHari) { this.jumlahHari = jumlahHari; }

    public String getAlasanTolak() { return alasanTolak; }
    public void setAlasanTolak(String alasanTolak) { this.alasanTolak = alasanTolak; }

    public LocalDate getDibuat() { return dibuat; }
    public void setDibuat(LocalDate dibuat) { this.dibuat = dibuat; }
}
