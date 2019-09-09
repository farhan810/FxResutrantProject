package tblcheck.service;

import java.util.List;

import tblcheck.model.ManagerStatus;
import tblcheck.model.RestRoomStatus;
import tblcheck.model.Section;
import tblcheck.model.Staff;
import tblcheck.model.Table;
import tblcheck.model.ResponseMessage.TableResponse;

/**
 * Created by snoy on 7/15/16.
 */
public interface ITableService {

    List<TableResponse> getTables() throws Exception;
    List<Section> getSections() throws Exception;
    List<Staff> getStaffs() throws Exception;

    boolean updateTable(Table table) throws Exception;
    boolean updateAllTables(List<Table> tables) throws Exception;
    List<Staff> addNewStaffs(List<Staff> staffs) throws Exception;
    boolean updateStaffs(List<Staff> staffs) throws Exception;
    boolean removeStaffs(List<Staff> staffs) throws Exception;

    boolean updateSectionNumber(int sections) throws Exception;
//    boolean updateSection(Section section) throws Exception;
    boolean addStaffToSection(Section section, Staff staff) throws Exception;
    boolean removeStaffFromSection(Section section, Staff staff) throws Exception;
    RestRoomStatus getRestroomStatus() throws Exception;
    boolean updateRestroomStatus(RestRoomStatus restRoomStatus) throws Exception;

    ManagerStatus getManagerStatus() throws Exception;
    boolean updateManagerStatus(ManagerStatus managerStatus) throws Exception;
}
