package com.tablecheck.service.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TableMapResponse {

	private String tableId;
	private int section;
	private String color;

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
