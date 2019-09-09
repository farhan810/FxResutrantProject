package tblcheck.model;

/**
 * Created by cuongdm5 on 1/24/2016.
 */
public class SectionAssignment {
    private int section;
    private String staffName;
    private boolean confirmed;

    public SectionAssignment(int section, String staffName) {
        this.section = section;
        this.staffName = staffName;
        this.confirmed = false;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public boolean isConfirmed() {
        return this.confirmed;
    }

    public void setConfirmed(boolean confirmed){
        this.confirmed = confirmed;
    }
}
