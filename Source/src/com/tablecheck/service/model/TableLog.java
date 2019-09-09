package com.tablecheck.service.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TableLog {
	public TableLog() {
	}

	public TableLog(String tableId, String color, int section) {
		this.setColor(color);
		this.setSection(section);
		this.setTableId(tableId);
	}

	private String date;
	private String tableId;
	private String color;
	private int section;

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public String getColor() {
		return color;
	}

	public int getSection() {
		return section;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		if (getDate() == null) {
			date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).format(new Date());
		}
		return String.format("%s, %s, %s, %s", date, tableId, color, section);
	}

}
