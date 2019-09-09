package com.tablecheck.service.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TableResponse implements Comparable<TableResponse> {
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

	@Override
	public int compareTo(TableResponse ob) {
		if (ob != null) {
			int s1 = Integer.parseInt(ob.tableId.substring(1));
			int s2 = Integer.parseInt(this.tableId.substring(1));
			return s2 == s1 ? 0 : (s2 > s1 ? 1 : -1);
		} else
			return -1;
	}

}
