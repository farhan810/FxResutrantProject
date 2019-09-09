package tblcheck.controller;

import java.util.List;

import com.tablecheck.service.model.TableResponse;
import com.tablecheck.service.model.TableResponseList;

import tblcheck.helper.CheckException;
import tblcheck.model.SectionAssignment;
import tblcheck.model.Table;

/**
 * Created by snoy on 7/3/16.
 */
public interface ITableController {
    void refreshTables();
    void refreshStaffList();
    void refreshStaffSection();
    boolean staffNumberChangeRequest(String val) throws CheckException;
    void updateTable(Table tbl);
    void updateAllTables(boolean rts, TableResponseList tables);
    void updateStaff(List<String> newStaffs) throws CheckException;
    String addStaffLog(SectionAssignment assignment, String action) throws CheckException;
    void exit();
    void start();
    void clear();
}
