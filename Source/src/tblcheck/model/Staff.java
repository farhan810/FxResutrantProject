package tblcheck.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snoy on 7/3/16.
 */
public class Staff {

    private int no;
    private String name;
    private List<Section> assignedSections;

    public int getNo(){return this.no;}
    public String getName(){return this.name;}
    public List<Section> getAssignedSections(){return this.assignedSections;}

    public Staff(String name){
        this.name = name;
    }

    public Staff(String name, Section assignedSection){
        this.name = name;
        this.assignedSections = new ArrayList<>();
        this.assignedSections.add(assignedSection);
    }

    public Staff(String name, List<Section> assignedSections){
        this.name = name;
        this.assignedSections = assignedSections;
    }
}
