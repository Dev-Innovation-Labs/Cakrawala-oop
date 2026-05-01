package id.universitas.cakrawala.workflow;

import id.universitas.cakrawala.domain.LeaveRequest;

/**
 * Guard: Syarat sebelum transisi boleh dilakukan
 */
public interface Guard {
    boolean boleh(LeaveRequest req);
    String alasanGagal();
}
