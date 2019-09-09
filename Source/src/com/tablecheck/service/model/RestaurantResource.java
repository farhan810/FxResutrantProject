package com.tablecheck.service.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.tablecheck.service.Constants;

import tblcheck.helper.DataHelper;

@XmlRootElement
public class RestaurantResource {

	private int sections;
	private int staffCount;
	private Map<String, Boolean> menuOptions;
	private Map<String, Boolean> managerState;
	private Map<String, Boolean> restroomState;
	private Map<Integer, String> sectionStaff;
	private List<String> staffs;

	public int getSections() {
		return sections;
	}

	public void setSections(int sections) {
		this.sections = sections;
	}

	public int getStaffCount() {
		return staffCount;
	}

	public void setStaffCount(int staffCount) {
		this.staffCount = staffCount;
	}

	public Map<String, Boolean> getMenuOptions() {
		if (menuOptions == null) {
			menuOptions = new HashMap<String, Boolean>();
		}
		return menuOptions;
	}

	public void setMenuOptions(Map<String, Boolean> menuOption) {
		DataHelper<Boolean> dataHelp = new DataHelper<>();
		getMenuOptions();
		this.menuOptions.put(Constants.menuShowNetStatus,
				dataHelp.getValueOrDefault(menuOption.get(Constants.menuShowNetStatus), false));
		this.menuOptions.put(Constants.menuShowServerHost,
				dataHelp.getValueOrDefault(menuOption.get(Constants.menuShowServerHost), false));
		this.menuOptions.put(Constants.menuShowclientHost,
				dataHelp.getValueOrDefault(menuOption.get(Constants.menuShowclientHost), false));
		this.menuOptions.put(Constants.menuShowTime,
				dataHelp.getValueOrDefault(menuOption.get(Constants.menuShowTime), false));
		this.menuOptions.put(Constants.menuShowState,
				dataHelp.getValueOrDefault(menuOption.get(Constants.menuShowState), false));
		this.menuOptions.put(Constants.menuShowRestroomStatus,
				dataHelp.getValueOrDefault(menuOption.get(Constants.menuShowRestroomStatus), false));
		this.menuOptions.put(Constants.menuShowManagerStatus,
				dataHelp.getValueOrDefault(menuOption.get(Constants.menuShowManagerStatus), false));
		this.menuOptions.put(Constants.menuShowStatusGlance,
				dataHelp.getValueOrDefault(menuOption.get(Constants.menuShowStatusGlance), false));
		this.menuOptions.put(Constants.menuLocked,
				dataHelp.getValueOrDefault(menuOption.get(Constants.menuLocked), false));
		this.menuOptions.put(Constants.menuClearSectionStaff,
				dataHelp.getValueOrDefault(menuOption.get(Constants.menuClearSectionStaff), false));
		this.menuOptions.put(Constants.menuClearTableSection,
				dataHelp.getValueOrDefault(menuOption.get(Constants.menuClearTableSection), false));
	}

	public Map<String, Boolean> getManagerState() {
		return managerState;
	}

	public void setManagerState(Map<String, Boolean> managerState) {
		this.managerState = managerState;
	}

	public Map<String, Boolean> getRestroomState() {
		return restroomState;
	}

	public void setRestroomState(Map<String, Boolean> restroomState) {
		this.restroomState = restroomState;
	}

	public List<String> getStaffs() {
		return staffs;
	}

	public void setStaffs(List<String> staffs) {
		this.staffs = staffs;
	}

	public Map<Integer, String> getSectionStaff() {
		return sectionStaff;
	}

	public void setSectionStaff(Map<Integer, String> sectionStaff) {
		this.sectionStaff = sectionStaff;
	}

}
