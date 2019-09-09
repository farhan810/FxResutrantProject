package tblcheck.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snoy on 7/3/16.
 */
public class Section {
    private int no;
    private List<Staff> staffs;

    public int getNo(){return this.no;}
    public List<Staff> getAssignedStaffs(){return this.staffs;}

    public Section() {}

    public Section(int no){
        this.no = no;
    }

    public Section(int no, Staff assignedStaff){
        this.no = no;
        this.staffs = new ArrayList<>();
        this.staffs.add(assignedStaff);
    }

    public Section(int no, List<Staff> staffs) {
        this.no = no;
        this.staffs = staffs;
    }
}
