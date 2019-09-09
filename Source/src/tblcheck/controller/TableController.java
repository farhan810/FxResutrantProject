package tblcheck.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.tablecheck.service.model.PingResponse;
import com.tablecheck.service.model.StaffLog;
import com.tablecheck.service.model.TableRequest;
import com.tablecheck.service.model.TableResponse;
import com.tablecheck.service.model.TableResponseList;

import tblcheck.helper.CheckException;
import tblcheck.helper.HttpHelper;
import tblcheck.helper.MulticastReceiver;
import tblcheck.helper.TypeHelper;
import tblcheck.model.Config;
import tblcheck.model.MessageRequest;
import tblcheck.model.MessageRequest.RequestCommand;
import tblcheck.model.SectionAssignment;
import tblcheck.model.Table;
import tblcheck.model.ResponseMessage.BaseResponseMessage;
import tblcheck.viewmodel.RoomViewModel;

/**
 * Created by cuongdm5 on 1/18/2016.
 */
public class TableController implements ITableController {

	private static Random random = new Random();
	private MessageTask task;
	private RoomViewModel roomViewModel;
	private String readRestroomUrl;
	private String readStaffNumberChangeUrl;
	private String pingUrl;
	private String readManagerUrl;
	private String readUrl;
	private String writeUrl;
	private String staffListUrl;
	private String staffLogUrl;
	private String staffSectionUrl;
	private String menuOptionUrl;
	private String pushNotificationUrl;

	private static final String encoder = "UTF-8";
	@Deprecated
	private final String STAFFLOG_FILE_NAME = "stafflog.txt";
	@Deprecated
	private final String STAFFLIST_FILE_NAME = "stafflist.txt";
        private ArrayList<TableRequest> bufferOfReq = new ArrayList<TableRequest>();
        private ArrayList<TableRequest> resultReq;
	public TableController(RoomViewModel viewModel) {
		this.roomViewModel = viewModel;
		task = new MessageTask(viewModel);
	}

	@Deprecated
	public void clear() {
		task.flush();
	}

	public void updateAllTables(boolean rts, TableResponseList tables) {
		// if (rts) {
		// ArrayList localArrayList = new ArrayList();
		// localArrayList.add(new BasicNameValuePair("ctn",
		// this.roomViewModel.encodeAllTables()));
		// addChangedQuery(localArrayList, "allgreen", rts);
		// // this.task.pushUpdate(this.writeUrl, localArrayList);
		// new Thread(() -> {
		// HttpHelper.sendPost(Config.getInstance().getPanelWriteUrl(),
		// localArrayList, false);
		// }).start();
		// } else {

		// List<NameValuePair> data = new ArrayList<>();
		// data.add(new BasicNameValuePair("ctn",
		// roomViewModel.encodeAllTables()));
		// addChangedQuery(data, "allgreen", rts);
		// task.pushUpdate(writeUrl, data);

		TableRequest req = new TableRequest();
		req.setAllDefault(true);
		// List<TableResponse> tables = roomViewModel.encodeTables();// new
		// ArrayList<>();
		// TableResponse table = new TableResponse();
		// table.setColor("allgreen");
		// tables.add(0, table);
		addChangedQuery(req, tables, rts);
                
            if (Config.getInstance().isServerConnected() && Config.getInstance().getServerHost()!=null) {                
                new Thread(() -> {
                    try {
                        HttpHelper.post(writeUrl, req);
                        task.getNotifier().notifySuccessRequest();
                    } catch (CheckException e) {
                        task.getNotifier().notifyFailRequest();
                        e.printStackTrace();
                    }
                }).start();
            }
            else {
                //disconnected
                bufferOfReq.add(req);
            }
	}
        
        public void postEventRequest() {
            if (Config.getInstance().isServerConnected() && Config.getInstance().getServerHost()!=null) {  
                System.out.println("size:" + bufferOfReq.size()); 
                if(bufferOfReq.size()>0){
                    while(bufferOfReq.size()>0) 
                    {
                        TableRequest firstReq = bufferOfReq.get(0);
                        try {
                            boolean pingResp = writePingReq(firstReq);
                            if (pingResp == true) {
                                //HttpHelper.post(writeUrl, firstReq);
                                task.getNotifier().notifySuccessRequest();
                                bufferOfReq.remove(0);
                            }else{
                                break;
                            }
                        } catch (CheckException e) {
                            task.getNotifier().notifyFailRequest();
                            e.printStackTrace();
                            break;
                        }
                    }                    
                }

            }
        }

	public void updateTable(Table table) {

            TableRequest req = new TableRequest();
            TableResponseList tables = new TableResponseList();
            tables.add(encode(table));
            addChangedQuery(req, tables, false);
            
            if (Config.getInstance().isServerConnected() && Config.getInstance().getServerHost()!=null) {                
                new Thread(() -> {
                    try {
                        HttpHelper.post(writeUrl, req);
                        task.getNotifier().notifySuccessRequest();
                    } catch (CheckException e) {
                        task.getNotifier().notifyFailRequest();
                        e.printStackTrace();
                    }
                }).start();
            }
            else {
                //disconnected
                bufferOfReq.add(req);
            }
	}

	public void updateSectionCount(int count) throws CheckException {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("sectionCount", "" + count));
		HttpHelper.post(Config.getInstance().getSectionCountUrl(), params);
	}

	private void addChangedQuery(TableRequest req, TableResponseList changed, boolean rts) {
		// int resId = 1, custId = 2;
		req.setRestaurantid("1");
		req.setCustomerId("2");
		req.setFrom("T");
		req.setTableChanges(changed);
		req.setLength(String.valueOf(roomViewModel.getTablesCount()));
		if (rts)
			req.setRTS("1");
		else
			req.setRTS("0");
		req.setT("0." + random.nextLong());

	}

	public TableResponse encode(Table tbl) {
		TableResponse tblResp = new TableResponse();
		tblResp.setColor(tbl.getColor().toLowerCase());
		tblResp.setSection(tbl.getSection());
		tblResp.setTableId(tbl.getTParameter());
		return tblResp;
	}

	public void refreshTables() {
		// task.pushRefresh(readUrl);
		try {
			String data = HttpHelper.get(readUrl);
			List<TableResponse> resp = (List<TableResponse>) HttpHelper.parseResponse(data, TypeHelper.RESP_TABLES);
			task.getNotifier().onMessageResponse(resp, RequestCommand.READ_TABLE);
		} catch (CheckException e) {
			task.getNotifier().notifyFailRequest();
			// return e.getType().name();
		}
	}

	public boolean lockMenus(boolean blLock) {
		try {
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("blLock", "" + blLock));
			String data = HttpHelper.post(Config.getInstance().getLockUrl(), params);
			return Boolean.parseBoolean(data);
		} catch (CheckException e) {
		}
		return false;
	}

	public PingResponse refreshPingStatus() throws CheckException {
		// task.pushRefresh(pingUrl);
		// try {
		String data = HttpHelper.get(pingUrl);
		PingResponse resp = (PingResponse) HttpHelper.parseResponse(data, PingResponse.class);
		// task.getNotifier().onMessageResponse(resp,
		// RequestCommand.READ_RESOURCES);
		// task.getNotifier().notifySuccessRequest();
		return resp;
		// } catch (CheckException e) {
		// task.getNotifier().notifyFailRequest();
		// }
		// return null;
	}
	public boolean writePingReq(TableRequest firstReq) throws CheckException {
		// task.pushRefresh(pingUrl);
		// try {
		String data = HttpHelper.post(writeUrl, firstReq);
                String s2 = "";
                data.equals(s2);
		if(data != null){
                    return true;
                }else{
                    Config.getInstance().setServerConnected(false);
                    return false;
                }

	}
	public void scanServer() {
            if (!Config.getInstance().isServerConnected()) {
                Config.getInstance().setServerHost(null);
            }
            //if (!Config.getInstance().isServerConnected() && !Config.getInstance().isServerChanging()) {// !Config.getInstance().isServer()
                                                                                                                                                                                                    // &&
            MulticastReceiver receiver = new MulticastReceiver();
            new Thread(receiver).start();
            //}
	}

	@Deprecated
	public void refreshRestroomStatus() {
		task.pushRequest(new MessageRequest(readRestroomUrl, RequestCommand.READ_RESTROOM));
	}

	@Deprecated
	public void refreshManagerStatus() {
		task.pushRequest(new MessageRequest(readManagerUrl, RequestCommand.READ_MANAGER));
	}

	private void addChangedQuery(List<NameValuePair> data, String changed, boolean rts) {
		// int resId = 1, custId = 2;
		data.add(new BasicNameValuePair("restaurantid", "1"));
		data.add(new BasicNameValuePair("customerid", "2"));
		data.add(new BasicNameValuePair("from", "T"));
		data.add(new BasicNameValuePair("tablechanged", changed));
		data.add(new BasicNameValuePair("length", String.valueOf(roomViewModel.getTablesCount())));
		if (rts)
			data.add(new BasicNameValuePair("RTS", "1"));
		data.add(new BasicNameValuePair("t", "0." + random.nextLong()));

	}

	@Deprecated
	public void refreshStaffList() {
		try {
			String data = HttpHelper.get(staffListUrl);
			// HttpHelper.parseResponse(data, type);
		} catch (CheckException e) {
			// return e.getType().name();
		}
		/*
		 * MessageRequest req = new MessageRequest(staffListUrl,
		 * MessageRequest.RequestCommand.READ_STAFF); task.pushRequest(req);
		 */
		// tempLoadStaff();
	}

	public void updateStaff(List<String> newStaffs) throws CheckException {

		String data = HttpHelper.post(staffListUrl, newStaffs);
		// HttpHelper.parseResponse(data, type);
		// return "Success";

		// MessageRequest req = new MessageRequest(staffListUrl,
		// RequestCommand.WRITE_STAFF, String.join("\n", newStaffs));
		// task.pushRequest(req);
		// tempUpdateStaff(newStaffs);
	}

	public boolean staffNumberChangeRequest(String val) throws CheckException {
		// System.out.println(val);
		System.out.println("staff_num=" + val);
		List<NameValuePair> data = new ArrayList<>();
		// data.add(new BasicNameValuePair("staff_num", val));
		data.add(new BasicNameValuePair("staffCount", val));
		// task.pushRequest(new MessageRequest(readStaffNumberChangeUrl,
		// RequestCommand.READ_STAFFNUMBERCHANGE, data));
		// Map<String, Integer> map = new HashMap();
		// map.put("staffCount", Integer.parseInt(val));
		return Boolean.parseBoolean(HttpHelper.post(readStaffNumberChangeUrl, data));
	}

	@Deprecated
	public void refreshStaffSection() {
		MessageRequest req = new MessageRequest(staffSectionUrl, MessageRequest.RequestCommand.READ_STAFFSECTION);
		task.pushRequest(req);
	}

	public void refreshPushNotification() {
		MessageRequest req = new MessageRequest(pushNotificationUrl,
				MessageRequest.RequestCommand.READ_PUSHNOTIFICATION);
		// task.pushRequest(req);
		try {
			String data = HttpHelper.get(req.getUrl());
			task.getNotifier().notifySuccessRequest();
			List<TableResponse> resp = (List<TableResponse>) HttpHelper.parseResponse(data, TypeHelper.RESP_TABLES);
			task.getNotifier().onMessageResponse(resp, MessageRequest.RequestCommand.READ_PUSHNOTIFICATION);
		} catch (CheckException e) {
			task.getNotifier().notifyFailRequest();
			e.printStackTrace();
		}
	}

	public void saveOptions(Map<String, Boolean> menuOptions) {
		new Thread(() -> {
			try {
				String data = HttpHelper.post(menuOptionUrl, menuOptions);
				task.getNotifier().notifySuccessRequest();
			} catch (CheckException e) {
				task.getNotifier().notifyFailRequest();
				e.printStackTrace();
			}
		}).start();

	}

	public String addStaffLog(SectionAssignment assignment, String action) {
		System.out.format("[TableController] add staff log: Section %d\t%s\t%s\n", assignment.getSection(),
				assignment.getStaffName(), action);
		StaffLog staffLog = new StaffLog();
		staffLog.setSection(String.valueOf(assignment.getSection()));
		staffLog.setStaffName(assignment.getStaffName());
		staffLog.setStatus(action);
		try {
			String data = HttpHelper.post(staffLogUrl, staffLog);
			// HttpHelper.parseResponse(data, type);
		} catch (CheckException e) {
			return e.getType().name();
		}
		return "Success";
		// List<NameValuePair> data = new ArrayList<>();
		// data.add(new BasicNameValuePair("employee",
		// assignment.getStaffName()));
		// data.add(new BasicNameValuePair("section",
		// String.valueOf(assignment.getSection())));
		// data.add(new BasicNameValuePair("action", action));
		// MessageRequest req = new MessageRequest(staffLogUrl,
		// MessageRequest.RequestCommand.WRITE_STAFF_LOG, data);

		// task.pushRequest(req);
		// temporaryLog(assignment, action);
	}

	public void start() {
		// Thread t = new Thread(task);
		// t.start();
	}

	public void exit() {
		this.task.exit();
	}

	private static String encode(String val) throws Exception {
		// return URLEncoder.encode(val, encoder);
		return val.replace("|", "%7C");
	}

	@Deprecated
	private void tempLoadStaff() {
		BufferedReader reader = null;
		String line = null;
		// List<String> staffs = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(STAFFLIST_FILE_NAME));
			while ((line = reader.readLine()) != null) {
				// staffs.add(line);
				sb.append(line);
				sb.append("\n");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		final Gson gson = new Gson();
		BaseResponseMessage resp = gson.fromJson(sb.toString(), BaseResponseMessage.class);
		this.roomViewModel.onMessageResponse(resp, RequestCommand.READ_STAFF);
	}

	@Deprecated
	private void tempUpdateStaff(List<String> newStaffs) {
		PrintWriter writer = null;
		try {
			File file = new File(STAFFLIST_FILE_NAME);
			writer = new PrintWriter(new FileOutputStream(file, false));
			for (String staff : newStaffs) {
				writer.write(staff);
				writer.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	@Deprecated
	private void temporaryLog(SectionAssignment ass, String action) {
		PrintWriter writer = null;
		try {
			File file = new File(STAFFLOG_FILE_NAME);
			writer = new PrintWriter(new FileOutputStream(file, true));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			writer.printf("%s,%s,%s,%s\r\n", dateFormat.format(new Date()), "Section " + ass.getSection(),
					ass.getStaffName(), action);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	public static long generateRandomNumber(int length) {
		char[] digits = new char[length];
		digits[0] = (char) (random.nextInt(9) + '1');
		for (int i = 1; i < length; i++) {
			digits[i] = (char) (random.nextInt(10) + '0');
		}
		return Long.parseLong(new String(digits));
	}

	public void setReadRestroomUrl(String readRestroomUrl) {
		this.readRestroomUrl = readRestroomUrl;
	}

	public void setPingUrl(String pingUrl) {
		this.pingUrl = pingUrl;
	}

	public void setReadStaffNumberChangeUrl(String readStaffNumberChangeUrl) {
		this.readStaffNumberChangeUrl = readStaffNumberChangeUrl;
	}

	public void setReadManagerUrl(String readManagerUrl) {
		this.readManagerUrl = readManagerUrl;
	}

	public void setPushNotificationUrl(String pushNotificationUrl) {
		this.pushNotificationUrl = pushNotificationUrl;
	}

	public void setReadUrl(String readUrl) {
		this.readUrl = readUrl;
	}

	public void setWriteUrl(String writeUrl) {
		this.writeUrl = writeUrl;
	}

	public void setStaffListUrl(String staffListUrl) {
		this.staffListUrl = staffListUrl;
	}

	public void setStaffLogUrl(String staffLogUrl) {
		this.staffLogUrl = staffLogUrl;
	}

	public void setStaffSectionUrl(String staffSectionUrl) {
		this.staffSectionUrl = staffSectionUrl;
	}

	public void setMenuOptionUrl(String menuOptionUrl) {
		this.menuOptionUrl = menuOptionUrl;
	}

}
