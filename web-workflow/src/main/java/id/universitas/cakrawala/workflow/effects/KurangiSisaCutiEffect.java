package id.universitas.cakrawala.workflow.effects;

import id.universitas.cakrawala.domain.LeaveRequest;
import id.universitas.cakrawala.workflow.TransitionEffect;

/**
 * Effect: Kurangi sisa cuti
 */
public class KurangiSisaCutiEffect implements TransitionEffect {
    @Override
    public void execute(LeaveRequest req) {
        int sisa = (int)(req.getSisaCuti() - req.getJumlahHari());
        req.setSisaCuti(sisa);
    }
}
