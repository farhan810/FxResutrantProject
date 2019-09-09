package com.tablecheck.service.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StaffLog {
	private String date;
	private String section;
	private String status;
	private String staffName;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	@Override
	public String toString() {
		if (getDate() == null) {
			date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).format(new Date());
		}
		return String.format("%s Section %s %s %s", date, section, status, staffName);
	}

}
