package tblcheck.model;

/**
 * Created by ASAX on 26-12-2016.
 */
public class ManagerStatus {
    private boolean isManagerNeededAtHostStand;
    private boolean isManagerNeededInKitchen;

    public ManagerStatus(boolean isManagerNeededAtHostStand, boolean isManagerNeededInKitchen){
        this.isManagerNeededAtHostStand = isManagerNeededAtHostStand;
        this.isManagerNeededInKitchen = isManagerNeededInKitchen;
    }

    public boolean isManagerNeededAtHostStand() {
        return isManagerNeededAtHostStand;
    }

    public boolean isManagerNeededInKitchen() {
        return isManagerNeededInKitchen;
    }
}
