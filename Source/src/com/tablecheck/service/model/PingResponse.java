package com.tablecheck.service.model;

import java.util.List;
import java.util.Set;

import tblcheck.model.Table;

public class PingResponse {
	private RestaurantResource resources;
	private List<TableResponse> tables;
	private boolean hold;
	private String layoutName;
	private List<Table> tableLayout;
	private Set<String> hostsToScan;
	private boolean shutInitiated;

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public List<Table> getTableLayout() {
		return tableLayout;
	}

	public void setTableLayout(List<Table> tableLayout) {
		this.tableLayout = tableLayout;
	}

	public boolean isHold() {
		return hold;
	}

	public void setHold(boolean hold) {
		this.hold = hold;
	}

	public RestaurantResource getResources() {
		return resources;
	}

	public void setResources(RestaurantResource resources) {
		this.resources = resources;
	}

	public List<TableResponse> getTables() {
		return tables;
	}

	public void setTables(List<TableResponse> tables) {
		this.tables = tables;
	}

	public Set<String> getHostsToScan() {
		return hostsToScan;
	}

	public void setHostsToScan(Set<String> hostsToScan) {
		this.hostsToScan = hostsToScan;
	}

	public boolean isShutInitiated() {
		return shutInitiated;
	}

	public void setShutInitiated(boolean shutInitiated) {
		this.shutInitiated = shutInitiated;
	}

}
