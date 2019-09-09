package tblcheck.model;

/**
 * Created by ASAX on 26-12-2016.
 */
public class RestRoomStatus {
    private boolean isMenStatusRed;
    private boolean isWomenStatusRed;

    public RestRoomStatus(boolean isMenStatusRed, boolean isWomenStatusRed){
        this.isMenStatusRed = isMenStatusRed;
        this.isWomenStatusRed = isWomenStatusRed;
    }

    public boolean isMenStatusRed() {
        return isMenStatusRed;
    }

    public boolean isWomenStatusRed() {
        return isWomenStatusRed;
    }

    @Override
    public String toString(){
        return "RestRoomStatus [isMenStatusRed - " + isMenStatusRed + ", isWomenStatusRed - " + isWomenStatusRed + "]";
    }
}
