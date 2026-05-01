package id.universitas.cakrawala.service;

import id.universitas.cakrawala.domain.LeaveRequest;
import id.universitas.cakrawala.domain.LeaveStatus;
import id.universitas.cakrawala.workflow.LeaveWorkflowEngine;
import id.universitas.cakrawala.workflow.Transition;
import id.universitas.cakrawala.workflow.guards.CutiCukupGuard;
import id.universitas.cakrawala.workflow.guards.MinimalH3Guard;
import id.universitas.cakrawala.workflow.guards.TanggalValidGuard;
import id.universitas.cakrawala.workflow.effects.KurangiSisaCutiEffect;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service untuk mengelola Leave Request dan Workflow
 */
@Service
public class LeaveRequestService {
    private final Map<String, LeaveRequest> requests = new HashMap<>();
    private final LeaveWorkflowEngine engine;

    public LeaveRequestService() {
        this.engine = initializeEngine();
    }

    /**
     * Inisialisasi workflow engine dengan semua transisi
     */
    private LeaveWorkflowEngine initializeEngine() {
        LeaveWorkflowEngine engine = new LeaveWorkflowEngine();

        // Transisi 1: DRAFT → DIAJUKAN
        engine.daftarTransisi(new Transition(
            "ajukan",
            LeaveStatus.DRAFT,
            LeaveStatus.DIAJUKAN,
            List.of(
                new CutiCukupGuard(),
                new TanggalValidGuard(),
                new MinimalH3Guard()
            ),
            List.of()
        ));

        // Transisi 2: DIAJUKAN → DISETUJUI_ATASAN
        engine.daftarTransisi(new Transition(
            "approve_atasan",
            LeaveStatus.DIAJUKAN,
            LeaveStatus.DISETUJUI_ATASAN,
            List.of(),
            List.of()
        ));

        // Transisi 3: DISETUJUI_ATASAN → DISETUJUI_HR
        engine.daftarTransisi(new Transition(
            "approve_hr",
            LeaveStatus.DISETUJUI_ATASAN,
            LeaveStatus.DISETUJUI_HR,
            List.of(),
            List.of(new KurangiSisaCutiEffect())
        ));

        // Transisi 4: DIAJUKAN → DITOLAK
        engine.daftarTransisi(new Transition(
            "tolak",
            LeaveStatus.DIAJUKAN,
            LeaveStatus.DITOLAK,
            List.of(),
            List.of()
        ));

        // Transisi 5: DRAFT → DIBATALKAN
        engine.daftarTransisi(new Transition(
            "batalkan",
            LeaveStatus.DRAFT,
            LeaveStatus.DIBATALKAN,
            List.of(),
            List.of()
        ));

        return engine;
    }

    /**
     * Buat request baru
     */
    public LeaveRequest createRequest(LeaveRequest req) {
        requests.put(req.getId(), req);
        return req;
    }

    /**
     * Dapatkan request berdasarkan ID
     */
    public Optional<LeaveRequest> getRequest(String id) {
        return Optional.ofNullable(requests.get(id));
    }

    /**
     * Dapatkan semua request
     */
    public List<LeaveRequest> getAllRequests() {
        return new java.util.ArrayList<>(requests.values());
    }

    /**
     * Jalankan transisi workflow
     */
    public LeaveWorkflowEngine.TransitionResult transisi(String requestId, String namaTransisi, String alasanTolak) {
        Optional<LeaveRequest> opt = getRequest(requestId);
        if (opt.isEmpty()) {
            return new LeaveWorkflowEngine.TransitionResult(
                false, "Request tidak ditemukan", null
            );
        }

        LeaveRequest req = opt.get();

        // Set alasan tolak jika diperlukan
        if ("tolak".equals(namaTransisi) && alasanTolak != null) {
            req.setAlasanTolak(alasanTolak);
        }

        return engine.jalankan(req, namaTransisi);
    }

    /**
     * Dapatkan workflow engine
     */
    public LeaveWorkflowEngine getEngine() {
        return engine;
    }
}
