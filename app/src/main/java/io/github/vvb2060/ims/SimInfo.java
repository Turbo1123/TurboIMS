package io.github.vvb2060.ims;

public class SimInfo {
    public int subId;
    public String displayName;
    public String carrierName;
    public int simSlotIndex;

    public SimInfo(int subId, String displayName, String carrierName, int simSlotIndex) {
        this.subId = subId;
        this.displayName = displayName;
        this.carrierName = carrierName;
        this.simSlotIndex = simSlotIndex;
    }

    @Override
    public String toString() {
        return String.format("SIM %d: %s (%s)", simSlotIndex + 1, displayName, carrierName);
    }
}
