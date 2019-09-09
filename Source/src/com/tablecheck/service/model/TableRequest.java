package com.tablecheck.service.model;

import org.springframework.boot.jackson.JsonComponent;

/**
 * @author lenovo
 *
 */
@JsonComponent
public class TableRequest {

	private String ctn;
	private String restaurantid;
	private String customerid;
	private String from;
	private String length;
	private String RTS;
	private String t;
	private boolean allDefault;
	private TableResponseList tableChanges;
	private String col;

	public String getRestaurantid() {
		return restaurantid;
	}

	public void setRestaurantid(String restaurantid) {
		this.restaurantid = restaurantid;
	}

	public String getCustomerId() {
		return customerid;
	}

	public void setCustomerId(String customerId) {
		this.customerid = customerId;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getRTS() {
		return RTS;
	}

	public void setRTS(String rTS) {
		RTS = rTS;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public TableResponseList getTableChanges() {
		return tableChanges;
	}

	public void setTableChanges(TableResponseList tableChanges) {
		this.tableChanges = tableChanges;
	}

	public boolean isAllDefault() {
		return allDefault;
	}

	public void setAllDefault(boolean allDefault) {
		this.allDefault = allDefault;
	}

	public String getCtn() {
		return ctn;
	}

	public void setCtn(String ctn) {
		this.ctn = ctn;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}
}
