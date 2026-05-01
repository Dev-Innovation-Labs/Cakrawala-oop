package id.universitas.cakrawala.controller;

import id.universitas.cakrawala.domain.LeaveRequest;
import id.universitas.cakrawala.dto.LeaveRequestDTO;
import id.universitas.cakrawala.service.LeaveRequestService;
import id.universitas.cakrawala.workflow.LeaveWorkflowEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST API Controller untuk Leave Request Workflow
 */
@RestController
@RequestMapping("/api/leave")
@CrossOrigin(origins = "*")
public class LeaveApiController {
    
    @Autowired
    private LeaveRequestService leaveService;

    /**
     * GET /api/leave/list - Dapatkan semua request
     */
    @GetMapping("/list")
    public ResponseEntity<List<LeaveRequestDTO>> getAllRequests() {
        List<LeaveRequestDTO> dtos = leaveService.getAllRequests()
            .stream()
            .map(LeaveRequestDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/leave/{id} - Dapatkan request berdasarkan ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRequest(@PathVariable String id) {
        Optional<LeaveRequest> opt = leaveService.getRequest(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(new LeaveRequestDTO(opt.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "Request tidak ditemukan"));
    }

    /**
     * POST /api/leave/create - Buat request baru
     */
    @PostMapping("/create")
    public ResponseEntity<?> createRequest(@RequestBody Map<String, Object> payload) {
        try {
            String nama = (String) payload.get("nama");
            String email = (String) payload.get("email");
            String mulaiStr = (String) payload.get("mulai");
            String selesaiStr = (String) payload.get("selesai");
            Integer sisaCuti = ((Number) payload.get("sisaCuti")).intValue();

            LocalDate mulai = LocalDate.parse(mulaiStr);
            LocalDate selesai = LocalDate.parse(selesaiStr);

            LeaveRequest req = new LeaveRequest(nama, email, mulai, selesai, sisaCuti);
            LeaveRequest created = leaveService.createRequest(req);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LeaveRequestDTO(created));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST /api/leave/{id}/transisi - Jalankan transisi workflow
     */
    @PostMapping("/{id}/transisi")
    public ResponseEntity<?> transisi(
            @PathVariable String id,
            @RequestBody Map<String, Object> payload) {
        try {
            String namaTransisi = (String) payload.get("transisi");
            String alasanTolak = (String) payload.get("alasanTolak");

            LeaveWorkflowEngine.TransitionResult result = 
                leaveService.transisi(id, namaTransisi, alasanTolak);

            if (result.isSukses()) {
                Optional<LeaveRequest> opt = leaveService.getRequest(id);
                if (opt.isPresent()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("sukses", true);
                    response.put("pesan", result.getPesan());
                    response.put("data", new LeaveRequestDTO(opt.get()));
                    return ResponseEntity.ok(response);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("sukses", false);
            response.put("pesan", result.getPesan());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("sukses", false, "error", e.getMessage()));
        }
    }

    /**
     * GET /api/leave/status/list - Dapatkan daftar semua status yang tersedia
     */
    @GetMapping("/status/list")
    public ResponseEntity<?> getStatusList() {
        List<Map<String, String>> statuses = List.of(
            Map.of("value", "DRAFT", "label", "Draf", "color", "#gray"),
            Map.of("value", "DIAJUKAN", "label", "Diajukan", "color", "#blue"),
            Map.of("value", "DISETUJUI_ATASAN", "label", "Disetujui Atasan", "color", "#cyan"),
            Map.of("value", "DISETUJUI_HR", "label", "Disetujui HR", "color", "#green"),
            Map.of("value", "DITOLAK", "label", "Ditolak", "color", "#red"),
            Map.of("value", "DIBATALKAN", "label", "Dibatalkan", "color", "#gray")
        );
        return ResponseEntity.ok(statuses);
    }

    /**
     * GET /api/leave/health - Health check
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "ok", "message", "Workflow Engine Running"));
    }
}
