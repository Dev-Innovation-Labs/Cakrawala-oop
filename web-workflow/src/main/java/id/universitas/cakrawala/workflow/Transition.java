package id.universitas.cakrawala.workflow;

import id.universitas.cakrawala.domain.LeaveStatus;
import id.universitas.cakrawala.domain.LeaveRequest;
import java.util.List;

/**
 * Transition: Definisi transisi status dengan guards dan effects
 */
public class Transition {
    private final String nama;
    private final LeaveStatus dari;
    private final LeaveStatus ke;
    private final List<Guard> guards;
    private final List<TransitionEffect> effects;

    public Transition(String nama, LeaveStatus dari, LeaveStatus ke,
                      List<Guard> guards, List<TransitionEffect> effects) {
        this.nama = nama;
        this.dari = dari;
        this.ke = ke;
        this.guards = guards != null ? guards : List.of();
        this.effects = effects != null ? effects : List.of();
    }

    public String getNama() { return nama; }
    public LeaveStatus getDari() { return dari; }
    public LeaveStatus getKe() { return ke; }
    public List<Guard> getGuards() { return guards; }
    public List<TransitionEffect> getEffects() { return effects; }
}
