package com.tablecheck.service.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tablecheck.service.Constants;
import com.tablecheck.service.model.PingResponse;
import com.tablecheck.service.model.RestaurantResource;
import com.tablecheck.service.model.StaffLog;
import com.tablecheck.service.model.TableLog;
import com.tablecheck.service.model.TableRequest;
import com.tablecheck.service.model.TableResponse;

import tblcheck.helper.ClientHelper;
import tblcheck.model.Config;
import tblcheck.view.MessageConstants;

@RestController
@RequestMapping("/tableservice")
public class ServiceController {
	private static final Logger logger = Logger.getLogger(ServiceController.class);
	private static String todayDate;
	private static final String file_table_state = Constants.file_table_state;
	private static final String file_table_map = Constants.file_table_map;
	private static String file_table_log = Constants.file_table_log;
	private static String file_staff_log = Constants.file_staff_log;

	private static final String file_restaurant_config = Constants.file_restaurant_config;

	ObjectMapper mapper;
	private static PingResponse ping;
	private static List<TableResponse> tables;
	// private static List<TableMapResponse> tableMaps;
	private static RestaurantResource restResources;

	private static int resourcesRespCnt = 0;

	public ServiceController() {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		// mapper.configure(SerializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
		// false);
		createNewDeleteOld();
	}

	@RequestMapping(path = "/ping", method = RequestMethod.GET, produces = "application/json")
	public PingResponse ping(
			@RequestParam(required = false, name = "openHold", defaultValue = "false") String openHold) {
		logger.debug("readResources started.....");
		if (ping == null) {
			ping = new PingResponse();
		}
		if ("true".equals(openHold)) {
			ping.setHold(true);
			ping.setLayoutName("");
		}
		ping.setResources(readResources());
		ping.setHostsToScan(Config.getInstance().getHostsToScan());
		ping.setTables(readTables());
		return ping;
	}

	@RequestMapping(path = "/shut", method = RequestMethod.POST, produces = "application/json")
	public PingResponse shut(@RequestParam String resetType) {
		logger.debug("readResources started.....");
		new Thread(() -> {
			if (MessageConstants.RESET_TYPE_PERM.equals(resetType)) {
                            Config.getInstance().setServer(false);
                            Config.getInstance().saveConfig(true, "readResources");
			}
			try {
                            ClientHelper.restartApplication(resetType, new Thread() {
                                    @Override
                                    public void run() {
                                            return;
                                    }
                            });
			} catch (IOException e) {
                            e.printStackTrace();
			}
		}).start();
		if (ping == null) {
			ping = new PingResponse();
		}
		ping.setShutInitiated(true);
		return ping("false");
	}

	@RequestMapping(path = "/restaurantresources", method = RequestMethod.GET, produces = "application/json")
	public RestaurantResource readResources() {
		logger.debug("readResources started.....");
		try {
			if (restResources == null) {
				byte[] bytes = Utility.readFileContent(file_restaurant_config);
				if (bytes != null && bytes.length > 0) {
					restResources = mapper.readValue(bytes, RestaurantResource.class);
					if (resourcesRespCnt == 0) {
						initResources();
					}
					resourcesRespCnt++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return restResources;
	}

	private void initResources() {
		if (restResources != null) {
			if (restResources.getRestroomState() != null)
				for (String stat : restResources.getRestroomState().keySet()) {
					restResources.getRestroomState().put(stat, false);
				}
			if (restResources.getManagerState() != null)
				for (String stat : restResources.getManagerState().keySet()) {
					restResources.getManagerState().put(stat, false);
				}
			if (restResources.getMenuOptions() != null)
				restResources.getMenuOptions().put(Constants.menuLocked, true);
			if (restResources.getStaffs() != null)
				restResources.setStaffCount(restResources.getStaffs().size());
		} else {
			restResources = defaultRestRes();
		}
		updateResources(restResources);
		ping.setHold(false);
	}

	@RequestMapping(path = "/restaurantresources", method = RequestMethod.POST, produces = "application/json")
	public boolean updateResources(@RequestBody RestaurantResource restResources) {
		logger.debug("updateResources started.....");
		try {
			return Utility.writeFileContent(file_restaurant_config, mapper.writeValueAsBytes(restResources));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping(path = "/menus", method = RequestMethod.POST, produces = "application/json")
	public boolean updateMenus(@RequestBody Map<String, Boolean> menuOptions) {
		logger.debug("updateMenus started....." + menuOptions);
		try {
			readResources().setMenuOptions(menuOptions);
			return Utility.writeFileContent(file_restaurant_config, mapper.writeValueAsBytes(restResources));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping(path = "/menus/lock", method = RequestMethod.POST, produces = "application/json")
	public boolean updateMenuLock(@RequestParam String blLock) {
		logger.debug("updateSectionsCount started.....");
		try {
			readResources().getMenuOptions().put(Constants.menuLocked, Boolean.parseBoolean(blLock));
			return Utility.writeFileContent(file_restaurant_config, mapper.writeValueAsBytes(restResources));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping(path = "/menus/hold", method = RequestMethod.POST, produces = "application/json")
	public boolean updateHold(@RequestParam String blHold) {
		logger.debug("updateSectionsCount started.....");
		try {

			return Utility.writeFileContent(file_restaurant_config, mapper.writeValueAsBytes(restResources));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping(path = "/staffs/counts", method = RequestMethod.POST, produces = "application/json")
	public boolean updateStaffsCount(@RequestParam int staffCount) {
		logger.debug("updateStaffsCount started.....");
		try {
			readResources().setStaffCount(staffCount);
			return Utility.writeFileContent(file_restaurant_config, mapper.writeValueAsBytes(restResources));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping(path = "/sections/counts", method = RequestMethod.POST, produces = "application/json")
	public boolean updateSectionsCount(@RequestParam String sectionCount) {
		logger.debug("updateSectionsCount started.....");
		try {
			readResources().setSections(Integer.parseInt(sectionCount));
			return Utility.writeFileContent(file_restaurant_config, mapper.writeValueAsBytes(restResources));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping(path = "/staffs", method = RequestMethod.GET, produces = "application/json")
	public List<String> getStaffs() {
		logger.debug("getStaffs started.....");
		return readResources().getStaffs();
	}

	@RequestMapping(path = "/staffs", method = RequestMethod.POST, produces = "application/json")
	public boolean updateStaffs(@RequestBody List<String> staffs) {
		logger.debug("updateStaffs started.....");
		try {
			restResources.setStaffs(staffs);
			return Utility.writeFileContent(file_restaurant_config, mapper.writeValueAsBytes(restResources));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping(path = "/managers/statuses", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Boolean> getManager() {
		logger.debug("getManager started.....");
		return readResources().getManagerState();
	}

	@RequestMapping(path = "/managers/statuses", method = RequestMethod.POST, produces = "application/json")
	public boolean updateManager(@RequestBody Map<String, Boolean> managerStat) {
		logger.debug("updateManager started.....");
		try {
			readResources().setManagerState(managerStat);
			return Utility.writeFileContent(file_restaurant_config, mapper.writeValueAsBytes(readResources()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping(path = "/restrooms", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Boolean> getRestRooms() {
		logger.debug("getRestRooms started.....");
		return readResources().getRestroomState();
	}

	@RequestMapping(path = "/restrooms", method = RequestMethod.POST, produces = "application/json")
	public boolean updateRest(@RequestBody Map<String, Boolean> restroomStat) {
		logger.debug("updateRest started.....");
		try {
			Map<String, Boolean> status = readResources().getRestroomState();

			if (((Boolean) status.get("M")) != restroomStat.get("M")) {
				status.put("M", restroomStat.get("M"));
			} else if (((Boolean) status.get("W")) != restroomStat.get("W")) {
				status.put("W", restroomStat.get("W"));
			} else {
				return false;
			}

			return Utility.writeFileContent(file_restaurant_config, mapper.writeValueAsBytes(readResources()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping(path = "/tablemap", method = RequestMethod.GET)
	public String readTableMap() {
		logger.debug("readTables started.....");
		String resp = "";
		try {
			byte[] bytes = Utility.readFileContent(file_table_map);
			if (bytes != null && bytes.length > 0) {
				resp = new String(bytes);
			}
			System.out.println("\n\n\n\n\t"+resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@RequestMapping(path = "/tables", method = RequestMethod.GET, produces = "application/json")
	public List<TableResponse> readTables() {
		logger.debug("readTables started.....");

		try {
			if (tables == null) {
				byte[] bytes = Utility.readFileContent(file_table_state);
				if (bytes != null && bytes.length > 0) {
					tables = mapper.readValue(bytes, new TypeReference<List<TableResponse>>() {
					});
				} else {
					tables = new ArrayList<>();
				}
			}
			return tables;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String validate(TableRequest req) {
		if (StringUtils.isEmpty(req.getRestaurantid())) {
			return "missing restaurantid data";
		} else if (StringUtils.isEmpty(req.getCustomerId())) {
			return "missing customerid data";
		} else if (StringUtils.isEmpty(req.getFrom())) {
			return "missing / invalid \'from\' data";
		} else if (StringUtils.isEmpty(req.getLength())) {
			return "missing 'length' data";
		} else if (!StringUtils.isEmpty(req.getLength())) {
			try {
				Integer.parseInt("" + req.getLength());
			} catch (Exception e) {
				return "invalid \'length\' data";
			}
		} else if (StringUtils.isEmpty(req.getTableChanges())) {
			// return "missing tablechanged data";
		} else if (StringUtils.isEmpty(req.getT())) {
			return "missing 't' data";
		}
		return "";
	}

	private static TableRequest tblRequest;

	@RequestMapping(path = "/table", method = RequestMethod.GET, produces = "application/json")
	public String readTable() {
		logger.debug("readTables started.....");
		// List<BasicNameValuePair> data = new ArrayList<BasicNameValuePair>();

		StringBuilder str = new StringBuilder();
		List<TableResponse> tables = readTables();
		Collections.sort(tables);
		for (TableResponse table : tables) {
			if (str.length() > 0) {
				str.append(" ~");
			}
			str.append(table.getTableId() + "=" + table.getColor() + "|" + table.getSection());
		}
		if (str.length() > 0) {
			str.append(" ");
		}
		// data.add(new BasicNameValuePair("ctn", str.toString()));
		try {
			if (tblRequest != null)
				System.out.println(mapper.writeValueAsString(tblRequest));
			else
				System.out.println("No RTS");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (tblRequest != null) {
			if (!"0".equals(tblRequest.getRTS()))
				str.append("RTS=1");// + tblRequest.getRTS());
			// data.add(new BasicNameValuePair("restaurantid",
			// tblRequest.getRestaurantid()));
			// data.add(new BasicNameValuePair("customerid",
			// tblRequest.getCustomerid()));
			// data.add(new BasicNameValuePair("from", tblRequest.getT()));
			// data.add(new BasicNameValuePair("tablechanged", "allgreen"));
			// data.add(new BasicNameValuePair("length",
			// tblRequest.getLength()));
			// data.add(new BasicNameValuePair("RTS", tblRequest.getRTS()));
			// data.add(new BasicNameValuePair("t", "0." + (new
			// Random()).nextLong()));
			tblRequest = null;
		}
		// logger.debug("readTable complete....." + str);
		return str.toString();// URLEncodedUtils.format(data,
								// Charset.defaultCharset());
	}

	@RequestMapping(path = "/tablehw", method = RequestMethod.POST)
	public ResponseEntity<Boolean> writeTable(@RequestBody TableRequest request) {
		logger.debug("writeTable started.....");

		try {
			if (validate(request).length() > 0 && request.getCtn() == null) {
				return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
			}
			String tableId = request.getCtn();
			String color = request.getCol();
			System.out.println(tableId + " >> " + color);
			TableResponse tblChanges = getTable(tables, tableId);
			if (tblChanges != null) {
				if (!tblChanges.getColor().equals(color)) {
					tblChanges.setColor(color);
					updateTableLogs(new TableLog(tableId, color, tblChanges.getSection()));
				} else {
					return new ResponseEntity<>(false, HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				tblChanges = new TableResponse();
				tblChanges.setTableId(tableId);
				tblChanges.setColor(color);
				tblChanges.setSection(0);
				tables.add(tblChanges);
				updateTableLogs(new TableLog(tableId, color, tblChanges.getSection()));
			}
			Utility.writeFileContent(file_table_state, mapper.writeValueAsBytes(tables));
			return new ResponseEntity<>(true, HttpStatus.OK);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(false, HttpStatus.EXPECTATION_FAILED);
	}

	@RequestMapping(path = "/tables", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Boolean> writeTables(@RequestBody TableRequest request) {
		logger.debug("writeTables started.....");

		try {
			if (validate(request).length() > 0) {
				return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
			}
			System.out.println("Write service >.... " + mapper.writeValueAsString(request));
			if (request.isAllDefault()) {
				ServiceController.tblRequest = request;
				tables = request.getTableChanges();
				// for (TableResponse table : tables) {
				// table.setColor("green");
				// }
				initResources();
			} else {
				for (TableResponse table : request.getTableChanges()) {
					TableResponse tblChanges = getTable(tables, table.getTableId());
					if (tblChanges != null) {
						if (!tblChanges.getColor().equals(table.getColor())
								|| tblChanges.getSection() != table.getSection()) {
							tblChanges.setColor(table.getColor());
							tblChanges.setSection(table.getSection());
							updateTableLogs(
									new TableLog(table.getTableId(), table.getColor(), tblChanges.getSection()));
						} else {
							return new ResponseEntity<>(false, HttpStatus.EXPECTATION_FAILED);
						}
					} else {
						tblChanges = new TableResponse();
						tblChanges.setTableId(table.getTableId());
						tblChanges.setColor(table.getColor());
						tblChanges.setSection(table.getSection());
						updateTableLogs(new TableLog(table.getTableId(), table.getColor(), tblChanges.getSection()));
						tables.add(tblChanges);
					}
				}
			}
			Utility.writeFileContent(file_table_state, mapper.writeValueAsBytes(tables));
			return new ResponseEntity<>(true, HttpStatus.OK);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
	}

	private TableResponse getTable(List<TableResponse> tables, String tableId) {
		if (tables != null)
			for (TableResponse table : tables) {
				if (tableId.equals(table.getTableId())) {
					return table;
				}
			}
		return null;
	}

	@RequestMapping(path = "/staffs/sections", method = RequestMethod.GET, produces = "application/json")
	public Map<Integer, String> readStaffsSections() {
		logger.debug("readTableMap started.....");
		return readResources().getSectionStaff();
	}

	@RequestMapping(path = "/staffs/sections", method = RequestMethod.POST, produces = "application/json")
	public boolean writeStaffsSections(@RequestBody Map<Integer, String> mapSections) {
		logger.debug("writeStaffsSections started.....");
		try {
			readResources().setSectionStaff(mapSections);
			return Utility.writeFileContent(file_restaurant_config, mapper.writeValueAsBytes(restResources));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	// @SuppressWarnings("unchecked")
	// @RequestMapping(path = "/tablemaps", method = RequestMethod.GET, produces
	// = "application/json")
	// public List<TableMapResponse> readTableMap() {
	// logger.debug("readTableMap started.....");
	// try {
	// if (tableMaps == null) {
	// tableMaps = mapper.readValue(Utility.readFileContent(file_table_map),
	// List.class);
	// }
	// return tableMaps;
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// @RequestMapping(path = "/tablemaps", method = RequestMethod.POST,
	// produces = "application/json")
	// public boolean writeTableMaps(@RequestBody List<TableMapResponse>
	// tableResponse) {
	// logger.debug("writeTableMaps started.....");
	//
	// try {
	// tableMaps = tableResponse;
	// return Utility.writeFileContent(file_table_map,
	// mapper.writeValueAsBytes(tableMaps));
	// } catch (JsonProcessingException e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

	@RequestMapping(path = "/tablelogs", method = RequestMethod.POST, produces = "application/json")
	public boolean updateTableLogs(@RequestBody TableLog tableLog) {
		logger.debug("updateTableLogs started.....");

		return Utility.appendToFile(getFile_table_log(), tableLog.toString());
	}

	@RequestMapping(path = "/stafflogs", method = RequestMethod.POST, produces = "application/json")
	public boolean updateStaffLogs(@RequestBody StaffLog staffLog) {
		logger.debug("updateStaffLogs started.....");

		Map<Integer, String> m = readStaffsSections();

		if ("stop".equals(staffLog.getStatus())) {
			m.remove(Integer.parseInt(staffLog.getSection()));
		} else {
			m.put(Integer.parseInt(staffLog.getSection()), staffLog.getStaffName());
		}
		writeStaffsSections(m);
		return Utility.appendToFile(getFile_staff_log(), staffLog.toString());
	}

	@RequestMapping(path = { "/logs/{filetype}/{stdate}/{enddate}",
			"/logs/{filetype}/{stdate}" }, method = RequestMethod.GET, produces = "application/octet-stream")
	public ResponseEntity<Resource> getAllLogs(@PathVariable("filetype") String filetype,
			@PathVariable("stdate") String stdate, @PathVariable(name = "enddate", required = false) String enddate) {
		logger.debug("getAllLogs started....." + filetype + " ?? " + stdate + " >> " + enddate);
		StringBuilder type = new StringBuilder();
		byte[] cont = Utility.downloadFiles(filetype, stdate, enddate, type);
		if (cont != null) {
			ByteArrayResource resource = new ByteArrayResource(cont);
			return ResponseEntity.ok().contentLength(resource.contentLength())
					.contentType(".zip".equalsIgnoreCase(type.toString()) ? MediaType.parseMediaType("application/zip")
							: MediaType.TEXT_PLAIN)
					.header("Content-Disposition",
							"attachment; filename=\""
									+ (".zip".equalsIgnoreCase(type.toString()) ? filetype + "_logs.zip" : type) + "\"")
					.body(resource);
		} else {
			String res = "Logs not available";
			return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(new ByteArrayResource(res.getBytes()));
		}

	}

	@RequestMapping(path = { "/logs/email/{filetype}/{stdate}/{enddate}",
			"/logs/email/{filetype}/{stdate}" }, method = RequestMethod.GET, produces = "application/json")
	public boolean emailAllLogs(@PathVariable("filetype") String filetype, @PathVariable("stdate") String stdate,
			@PathVariable(name = "enddate", required = false) String enddate,
			@RequestParam(value = "email") String email) {
		logger.debug("emailAllLogs started.....");
		return Utility.emailFiles(filetype, stdate, enddate, email);
	}

	@RequestMapping(path = "/time", method = RequestMethod.GET)
	public String getTime() {
		logger.debug("getTime started.....");

		return new SimpleDateFormat("dd mm yyyy HH:mm:ss").format(new Date());
	}

	public String getFile_table_log() {
		createNewDeleteOld();
		return file_table_log;
	}

	public String getFile_staff_log() {
		createNewDeleteOld();
		return file_staff_log;
	}

	private void createNewDeleteOld() {
		if (!getTodayDate().equals(todayDate)) {
			todayDate = getTodayDate();
			file_table_log = "table_log" + todayDate + ".txt";
			file_staff_log = "staff_log" + todayDate + ".txt";
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -(Config.getInstance().getLogsRetainDays()));
			Utility.deleteFiles(cal.getTime());
		}
	}

	// When server starts, all tables should be in gree
	// all section should be cleared
	// Or loaded as per the latest updated yesterday or previous shut
	private String getTodayDate() {
		
		
		System.out.println("im here server farhan startedd!!!!");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, Config.getInstance().getLogsDayHours());
		return new SimpleDateFormat("ddMMyyyy").format(cal.getTime());
	}

	// private String convertDate(String date) {
	// SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	// try {
	// Date dt = sdf.parse(date);
	// sdf.applyPattern("ddMMyyyy");
	// String dts = sdf.format(dt);
	// return dts;
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	public static void main(String[] args) {
		ServiceController con = new ServiceController();

		RestaurantResource res = con.readResources();// new
														// RestaurantResource();
		Map<String, Boolean> menuOPtions = new HashMap<>();
		menuOPtions.put("ShowNetStatus", false);
		menuOPtions.put("ShowTime", false);
		menuOPtions.put("ShowState", false);
		menuOPtions.put("ShowRestRoomStatus", false);
		menuOPtions.put("ShowManagerStatus", false);
		menuOPtions.put("ShowWaitStaff", false);
		menuOPtions.put(Constants.menuLocked, true);
		menuOPtions.put(Constants.menuShowStatusGlance, false);
		res.setMenuOptions(menuOPtions);

		//res.setSections(7);
                //Updated by Han 
                res.setSections(15);
		con.updateResources(res);

		// Map<String, Boolean> managersState = new HashMap<>();
		// managersState.put("K", false);
		// managersState.put("H", false);
		// res.setManagerState(managersState);
		//
		// managersState = new HashMap<>();
		// managersState.put("M", false);
		// managersState.put("F", false);
		// res.setRestroomState(managersState);
		//
		// List<Boolean> managerState = new ArrayList<>();
		// managerState.add(false);
		// managerState.add(false);
		// managerState.add(false);
		//
		// List<String> managerssState = new ArrayList<>();
		// managerssState.add("V");
		// managerssState.add("FSDFSD");
		// managerssState.add("gA");
		// res.setStaffs(managerssState);
		// res.setStaffCount(8);
		//con.updateResources(res);

		// List<TableResponse> tbls = new ArrayList<>();
		// TableResponse tbl = new TableResponse();
		// tbl.setColor("red");
		// tbl.setSection(2);
		// tbl.setTableId("T1");
		// tbls.add(tbl);
		// tbl = new TableResponse();
		// tbl.setColor("green");
		// tbl.setSection(2);
		// tbl.setTableId("T2");
		// tbls.add(tbl);
		// try {
		//// Utility.writeFileContent(file_table_state,
		// con.mapper.writeValueAsBytes(tbls));
		// } catch (JsonProcessingException e) {
		// e.printStackTrace();
		// }
		// con.writeTables(tbls);
		// List<TableMapResponse> tblms = new ArrayList<>();
		// TableMapResponse tblm = new TableMapResponse();
		// tblm.setColor("red");
		// tblm.setSection(2);
		// tblm.setTableId("1234");
		// tblms.add(tblm);

		// con.writeTableMaps(tblms);

	}

	private RestaurantResource defaultRestRes() {
		RestaurantResource res = new RestaurantResource();
		Map<String, Boolean> menuOPtions = new HashMap<>();
		menuOPtions.put("ShowNetStatus", true);
		menuOPtions.put("ShowTime", false);
		menuOPtions.put("ShowState", false);
		menuOPtions.put("ShowRestRoomStatus", false);
		menuOPtions.put("ShowManagerStatus", false);
		menuOPtions.put("ShowWaitStaff", false);
		menuOPtions.put(Constants.menuLocked, true);
		menuOPtions.put(Constants.menuShowStatusGlance, false);
		res.setMenuOptions(menuOPtions);

		res.setSections(7);

		Map<String, Boolean> managersState = new HashMap<>();
		managersState.put("K", false);
		managersState.put("H", false);
		res.setManagerState(managersState);

		managersState = new HashMap<>();
		managersState.put("M", false);
		managersState.put("W", false);
		res.setRestroomState(managersState);

		List<String> managerssState = new ArrayList<>();
		managerssState.add("Staff1");
		res.setStaffs(managerssState);
		res.setStaffCount(1);

		return res;
	}
}
