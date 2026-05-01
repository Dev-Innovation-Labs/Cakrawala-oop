package id.universitas.cakrawala.workflow;

import id.universitas.cakrawala.domain.LeaveRequest;

/**
 * Effect: Aksi yang dilakukan setelah transisi berhasil
 */
public interface TransitionEffect {
    void execute(LeaveRequest req);
}
