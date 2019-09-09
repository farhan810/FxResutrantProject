package tblcheck.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

//import com.sun.tools.javac.util.Pair;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tablecheck.client.main.Main;

import tblcheck.helper.CheckException;
import tblcheck.helper.HttpHelper;
import tblcheck.helper.LayoutHelper;
import tblcheck.model.Config;
import tblcheck.model.ManagerStatus;
import tblcheck.model.RestRoomStatus;
import tblcheck.model.Section;
import tblcheck.model.Staff;
import tblcheck.model.Table;
import tblcheck.model.ResponseMessage.BaseResponseMessage;
import tblcheck.model.ResponseMessage.TableResponse;

/**
 * Created by snoy on 7/15/16.
 */
public class TableService implements ITableService {

	private static final Type RESP_STR = new TypeToken<BaseResponseMessage<String>>() {
	}.getType();
	private static final Type RESP_SEC = new TypeToken<BaseResponseMessage<Section>>() {
	}.getType();
	private static final Type RESP_ARR_SEC = new TypeToken<BaseResponseMessage<List<Section>>>() {
	}.getType();
	private static final Type RESP_STAFF = new TypeToken<BaseResponseMessage<Staff>>() {
	}.getType();
	private static final Type RESP_ARR_STAFF = new TypeToken<BaseResponseMessage<List<Staff>>>() {
	}.getType();
	private static final String STR_BREAK = "-------------------------------------\n";
	private static final Gson gson = new Gson();
	private static final Random random = new Random();
	private static final ITableService INSTANCE = new TableService();

	public static ITableService getInstance() {
		return INSTANCE;
	}

	public static void mains(String[] args) {
		Properties prop = com.tablecheck.client.main.Main.loadProperties(Config.CONFIG_FILE);
		Config.loadProperty(prop);

		ITableService tblService = getInstance();
		// ITableService tblService = new TableService();
		try {
			System.out.println(STR_BREAK + "TABLES\n");
			List<TableResponse> tbls = tblService.getTables();
			for (TableResponse tbl : tbls) {
				System.out.println(String.format("ID: %s\tColor: %s\tSection: %d", tbl.getTParam(), tbl.getColor(),
						tbl.getSection()));
			}

			System.out.println(STR_BREAK + "SECTION\n");
			List<Section> sects = tblService.getSections();
			for (Section sec : sects) {
				System.out.println("Section ID: " + sec.getNo());
				System.out.println("Assigned Staffs:");
				for (Staff staff : sec.getAssignedStaffs()) {
					System.out.println(String.format("ID: %d\tName: %s", staff.getNo(), staff.getName()));
				}
			}

			System.out.println(STR_BREAK + "STAFFS\n");
			List<Staff> staffs = tblService.getStaffs();
			for (Staff staff : staffs) {
				System.out.println(String.format("ID: %d\tName: %s", staff.getNo(), staff.getName()));
			}

			System.out.printf(STR_BREAK + "UPDATE A TABLE\n");
			Table tbl = new Table("1", 1, 1, 1, 1, 1);
			tbl.setTParameter("T1");
			tbl.setId("1");
			tbl.setState(Table.State.IN_USE);
			tblService.updateTable(tbl);
			Thread.sleep(4000);
			tbl.setState(Table.State.DONE);
			tblService.updateTable(tbl);
			Thread.sleep(4000);
			tbl.setState(Table.State.READY);
			tblService.updateTable(tbl);
			Thread.sleep(4000);

			System.out.printf(STR_BREAK + "UPDATE ALL TABLES\n");
			List<Table> alltables = LayoutHelper.getTables(Config.getInstance().getLayout());
			List<Table> tables = new ArrayList<>();
			for (Table tb : alltables) {
				if (tb.getType() == Table.Type.TABLE)
					tables.add(tb);
			}
			LayoutHelper.sortTables(tables);
			for (Table tb : tables) {
				tb.setState(Table.State.DONE);
			}
			tblService.updateAllTables(tables);
			Thread.sleep(4000);
			for (Table tb : tables) {
				tb.setState(Table.State.READY);
			}
			tblService.updateAllTables(tables);

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	@Override
	public List<TableResponse> getTables() throws Exception {
		String data = HttpHelper.sendGet(Config.getInstance().getReadUrl());
		if (data != null && !data.isEmpty()) {
			BaseResponseMessage<String> res = gson.fromJson(data, RESP_STR);
			if (res != null && res.getStatus().equalsIgnoreCase(BaseResponseMessage.STT_OK)) {
				String resData = res.getData();
				List<TableResponse> retVal = new ArrayList<>();
				String[] tbls = resData.split("~");
				String tParam, color;
				int section;
				try {
					for (String table : tbls) {
						String[] arr = table.trim().split("[=\\| ]");
						tParam = arr[0];
						color = arr[1];
						section = Integer.parseInt(arr[2]);
						// System.out.println(String.format("%s\t%s\t%d",
						// tParam,color, section));
						retVal.add(new TableResponse(tParam, color, section));
					}
				} catch (Exception e) {
					System.out.println("## parse error ##");
					System.out.println(
							"Update Error.\nA parse error occurred (The output from the server is not formatted correctly.)");
					e.printStackTrace();
				}
				return retVal;
			}
		}
		return null;
	}

	@Override
	public RestRoomStatus getRestroomStatus() throws Exception {
		Config config = Config.getInstance();
		System.out.println("get - Config - " + config);
		String url = config.getReadRestroomUrl();
		System.out.println("get - URL - " + url);
		String data = HttpHelper.sendGet(url);
		if (data != null && !data.isEmpty()) {
			BaseResponseMessage<String> res = gson.fromJson(data, RESP_STR);
			if (res != null && res.getStatus().equalsIgnoreCase(BaseResponseMessage.STT_OK)) {
				String resData = res.getData();
				String[] restRoomStatus = resData.split("~");
				RestRoomStatus retVal = new RestRoomStatus(Boolean.parseBoolean(restRoomStatus[0]),
						Boolean.parseBoolean(restRoomStatus[1]));
				return retVal;
			}
		}
		return null;
		/*
		 * return new RestRoomStatus(isMenRestroomStatusRed,
		 * isWomenRestroomStatusRed);
		 */
	}

	// Mimic restroom status from server side.
	/*
	 * private boolean isMenRestroomStatusRed = false; private boolean
	 * isWomenRestroomStatusRed = false;
	 */

	@Override
	public boolean updateRestroomStatus(RestRoomStatus restRoomStatus) throws Exception {
		System.out.println("updateRestroomStatus");
		Config config = Config.getInstance();
		System.out.println("post - Config - " + config);
		String url = config.getWriteRestroomUrl();
		System.out.println("Test - " + url);
		// List<NameValuePair> data = new ArrayList<>();
		// data.add(new BasicNameValuePair("M", "" +
		// restRoomStatus.isMenStatusRed())); //encode(table.encode(true))
		// data.add(new BasicNameValuePair("W", "" +
		// restRoomStatus.isWomenStatusRed()));

		Map<String, Boolean> data = new HashMap<>();
		data.put("M", restRoomStatus.isMenStatusRed());
		data.put("W", restRoomStatus.isWomenStatusRed());
		System.out.println("updateRestroomStatus Post data - " + data);
		// String resp = HttpHelper.sendPost(url, data);
		String resp = HttpHelper.post(url, data);
		System.out.println("updateRestroomStatus request - " + restRoomStatus);
		System.out.println("updateRestroomStatus response - " + resp);
		return Boolean.parseBoolean(resp);
		// if(Boolean.parseBoolean(resp)){
		//
		// }
		// if(resp != null && !resp.isEmpty()){
		// BaseResponseMessage<String> res = gson.fromJson(resp, RESP_STR);
		// System.out.println("updateRestroomStatus res - " + res);
		// if(res!=null){
		// System.out.println("updateRestroomStatus res.getStatus() - " +
		// res.getStatus());
		// }
		//
		// return res != null &&
		// res.getStatus().equalsIgnoreCase(BaseResponseMessage.STT_OK);
		// }
		// return false;
		/*
		 * isMenRestroomStatusRed = restRoomStatus.isMenStatusRed();
		 * isWomenRestroomStatusRed = restRoomStatus.isWomenStatusRed(); return
		 * true;
		 */
	}

	// Mimic manager status from server side.
	/*
	 * private boolean isManagerNeededAtHostStand = false; private boolean
	 * isManagerNeededInKitchen = false;
	 */

	public ManagerStatus getManagerStatus() throws Exception {
		String data = HttpHelper.sendGet(Config.getInstance().getReadManagerUrl());
		if (data != null && !data.isEmpty()) {
			BaseResponseMessage<String> res = gson.fromJson(data, RESP_STR);
			if (res != null && res.getStatus().equalsIgnoreCase(BaseResponseMessage.STT_OK)) {
				String resData = res.getData();
				String[] managerStatus = resData.split("~");
				ManagerStatus retVal = new ManagerStatus(Boolean.parseBoolean(managerStatus[0]),
						Boolean.parseBoolean(managerStatus[1]));
				return retVal;
			}
		}
		return null;
		/*
		 * return new ManagerStatus(isManagerNeededAtHostStand,
		 * isManagerNeededInKitchen);
		 */
	}

	public boolean updateManagerStatus(ManagerStatus managerStatus) throws Exception {
		// List<NameValuePair> data = new ArrayList<>();
		// data.add(new BasicNameValuePair("H", "" +
		// managerStatus.isManagerNeededAtHostStand()));
		// //encode(table.encode(true))
		// data.add(new BasicNameValuePair("K", "" +
		// managerStatus.isManagerNeededInKitchen()));
		// String resp =
		// HttpHelper.sendPost(Config.getInstance().getWriteManagerUrl(), data);
		// if(resp != null && !resp.isEmpty()){
		// BaseResponseMessage<String> res = gson.fromJson(resp, RESP_STR);
		// return res != null &&
		// res.getStatus().equalsIgnoreCase(BaseResponseMessage.STT_OK);
		// }
		Map<String, Boolean> man = new HashMap<>();
		man.put("H", managerStatus.isManagerNeededAtHostStand());
		man.put("K", managerStatus.isManagerNeededInKitchen());
		return Boolean.parseBoolean(HttpHelper.post(Config.getInstance().getWriteManagerUrl(), man));
		/*
		 * isManagerNeededAtHostStand =
		 * managerStatus.isManagerNeededAtHostStand(); isManagerNeededInKitchen
		 * = managerStatus.isManagerNeededInKitchen(); return true;
		 */
	}

	@Override
	@Deprecated
	public List<Section> getSections() throws Exception {
		String data = HttpHelper.sendGet(Config.getInstance().getSectionUrl());
		if (data != null && !data.isEmpty()) {
			BaseResponseMessage<List<Section>> res = gson.fromJson(data, RESP_ARR_SEC);
			if (res != null && res.getStatus().equalsIgnoreCase(BaseResponseMessage.STT_OK)) {
				return res.getData();
			}
		}
		return null;
	}

	@Override
	@Deprecated
	public List<Staff> getStaffs() throws Exception {
		try {
			String data = HttpHelper.get(Config.getInstance().getStaffUrl());
			// HttpHelper.parseResponse(data, type);
			if (data != null && !data.isEmpty()) {
				BaseResponseMessage<List<Staff>> res = gson.fromJson(data, RESP_ARR_STAFF);
				if (res != null && res.getStatus().equalsIgnoreCase(BaseResponseMessage.STT_OK)) {
					return res.getData();
				}
			}
		} catch (CheckException e) {
			// return e.getType().name();
		}
		// String data = HttpHelper.sendGet(Config.getInstance().getStaffUrl());

		return null;
	}

	@Override
	@Deprecated
	public boolean updateTable(Table table) throws Exception {
		List<NameValuePair> data = new ArrayList<>();
		data.add(new BasicNameValuePair("ctn", table.encode(true))); // encode(table.encode(true))
		addChangedQuery(data, table.encode(false), false);
		String resp = HttpHelper.sendPost(Config.getInstance().getWriteUrl(), data);
               // System.out.println("yyy:" + Config.getInstance().isServerConnected());
		if (resp != null && !resp.isEmpty()) {
			BaseResponseMessage<String> res = gson.fromJson(resp, RESP_STR);
			return res != null && res.getStatus().equalsIgnoreCase(BaseResponseMessage.STT_OK);
		}else{
                   // return "error";
                }
		return false;
	}

	@Deprecated
	@Override
	public boolean updateAllTables(List<Table> tables) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (Table tbl : tables) {
			sb.append(tbl.encode(true));
			sb.append("~");
		}
		if (sb.length() > 0)
			sb.setLength(sb.length() - 1);
		List<NameValuePair> data = new ArrayList<>();
		data.add(new BasicNameValuePair("ctn", sb.toString())); // encode(table.encode(true))
		addChangedQuery(data, "alltables", false);
		String resp = HttpHelper.sendPost(Config.getInstance().getWriteUrl(), data);
		if (resp != null && !resp.isEmpty()) {
			BaseResponseMessage<String> res = gson.fromJson(resp, RESP_STR);
			return res != null && res.getStatus().equalsIgnoreCase(BaseResponseMessage.STT_OK);
		}
		return false;
	}

	private void addChangedQuery(List<NameValuePair> data, String changed, boolean rts) {
		data.add(new BasicNameValuePair("restaurantid", "1"));
		data.add(new BasicNameValuePair("customerid", "2"));
		data.add(new BasicNameValuePair("from", "T"));
		data.add(new BasicNameValuePair("tablechanged", changed));
		if (rts)
			data.add(new BasicNameValuePair("RTS", "1"));
		data.add(new BasicNameValuePair("t", "0." + random.nextLong()));

	}

	@Override
	public List<Staff> addNewStaffs(List<Staff> staffs) throws Exception {
		List<String> names = new ArrayList<>();
		for (Staff staff : staffs) {
			names.add(staff.getName());
		}
		List<NameValuePair> data = new ArrayList<>();
		data.add(new BasicNameValuePair("names", gson.toJson(names))); // encode(table.encode(true))
		String resp = HttpHelper.sendPost(Config.getInstance().getStaffUrl(), data);
		if (resp != null && !resp.isEmpty()) {
			BaseResponseMessage<List<Staff>> res = gson.fromJson(resp, RESP_ARR_STAFF);
			if (res != null && res.getStatus().equalsIgnoreCase(BaseResponseMessage.STT_OK))
				return res.getData();
		}
		return null;
	}

	@Override
	public boolean updateStaffs(List<Staff> staffs) throws Exception {

		return false;
	}

	@Override
	public boolean removeStaffs(List<Staff> staffs) throws Exception {
		return false;
	}

	@Override
	public boolean updateSectionNumber(int sections) throws Exception {
		return false;
	}

	@Override
	public boolean addStaffToSection(Section section, Staff staff) throws Exception {
		return false;
	}

	@Override
	public boolean removeStaffFromSection(Section section, Staff staff) throws Exception {
		return false;
	}
}
