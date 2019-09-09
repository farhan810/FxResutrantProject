package tblcheck.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import tblcheck.helper.HttpHelper;

/**
 * Created by cuongdm5 on 1/11/2016.
 */
public class Config {
	public static final String CONFIG_FILE = "tablecheck.cfg";
	public static boolean tempClient;
        int nRThreadStatus = 0;  //Receiver Thread 0-running, 1-stopping, 2-stopped
        int nBThreadStatus = 0;  //Broadcast Thread 0-running, 1-stopping, 2-stopped
        long nLThreadTime = 0;
	boolean server=false;
	boolean client=true;
        boolean bSwipeStatus = false;
	String serverHost;
	Set<String> hostsToScan;
	String clientHost;
	int serverPort;
	String readRestroomUrl;
	String pushNotificationUrl;
	String writeRestroomUrl;
	String readManagerUrl;
	String writeManagerUrl;
	String readStaffNumberChangeUrl;
	String pingUrl;
	String readUrl;
	String menuOptionUrl;
	String writeUrl;
	String staffLogUrl;
	String staffListUrl;
	String staffSectionUrl;
	String sectionCountUrl;
	String staffUrl;
	String sectionUrl;
	String lockUrl;
	String panelWriteUrl;
	String serverShutUrl;
	String password;
	int sections;
	double zoomFactor;
	int logsRetainDays;
	int logsDayHours;
	String layout;
	String layoutFolder;
	boolean showKeyboard;
	boolean useTabtip;
	boolean logMessage;
	String serverVersion;
	String logDirectory;

	String email_from;
	String email_sub;
	String email_content;
	String email_smtp_host;
	int email_smtp_port;
	String email_protocol;
	boolean email_tls_enabled;
	String email_smtp_username;
	String email_smtp_password;
	boolean hideResets;
	Set<String> servers;
	int broadcastPort;
	String broadcastServerHost;
	boolean serverConnected=false;

	private boolean ServerChanging;

	public String getReadRestroomUrl() {
		return readRestroomUrl;
	}

	public String getPingUrl() {
		return pingUrl;
	}

	public String getPushNotificationUrl() {
		return pushNotificationUrl;
	}

	public String getWriteRestroomUrl() {
		return writeRestroomUrl;
	}

	public String getReadManagerUrl() {
		return readManagerUrl;
	}

	public String getWriteManagerUrl() {
		return writeManagerUrl;
	}

	public String getReadUrl() {
		return readUrl;
	}

	public String getSectionUrl() {
		return sectionUrl;
	}

	public void setPushNotificationUrl(String pushNotificationUrl) {
		this.pushNotificationUrl = pushNotificationUrl;
	}

	public void setReadUrl(String readUrl) {
		this.readUrl = readUrl;
	}

	public void setReadStaffNumberChangeUrl(String readStaffNumberChangeUrl) {
		this.readStaffNumberChangeUrl = readStaffNumberChangeUrl;
	}

	public String getReadStaffNumberChangeUrl() {
		return readStaffNumberChangeUrl;
	}

	public String getWriteUrl() {
		return writeUrl;
	}

	public void setWriteUrl(String writeUrl) {
		this.writeUrl = writeUrl;
	}

	public String getStaffLogUrl() {
		return staffLogUrl;
	}

	public void setStaffLogUrl(String staffLogUrl) {
		this.staffLogUrl = staffLogUrl;
	}

	public String getServerShutUrl() {
		return serverShutUrl;
	}

	public void setServerShutUrl(String serverShutUrl) {
		this.serverShutUrl = serverShutUrl;
	}

	public String getStaffListUrl() {
		return staffListUrl;
	}

	public void setStaffListUrl(String staffListUrl) {
		this.staffListUrl = staffListUrl;
	}

	public String getStaffUrl() {
		return staffUrl;
	}

	public void setStaffSectionUrl(String staffSectionUrl) {
		this.staffSectionUrl = staffSectionUrl;
	}

	public String getStaffSectionUrl() {
		return staffSectionUrl;
	}

	public void setStaffUrl(String staffUrl) {
		this.staffUrl = staffUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSections() {
		return sections;
	}

	public void setSections(int sections) {
		this.sections = sections;
	}

	public double getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public boolean isShowKeyboard() {
		return showKeyboard;
	}

	public void setShowKeyboard(boolean showKeyboard) {
		this.showKeyboard = showKeyboard;
	}

	public boolean isUseTabtip() {
		return useTabtip;
	}

	public void setUseTabtip(boolean useTabtip) {
		this.useTabtip = useTabtip;
	}

	public boolean isLogMessage() {
		return logMessage;
	}

	public void setLogMessage(boolean logMessage) {
		this.logMessage = logMessage;
	}

	public Config() {
	}

	public Config(Properties properties) {
		this.server = properties.getProperty("system_as_server", "false").equals("true");
		if (tempClient) {
			this.server = false;
		}
		this.client = properties.getProperty("system_as_client", "true").equals("true");
                this.bSwipeStatus = properties.getProperty("swipe_status", "false").equals("true");
                
		this.serverPort = Integer.parseInt(properties.getProperty("server_port", "8080"));
		this.serverHost = properties.getProperty("server_host", "localhost");
		if (this.serverHost.contains(";")) {
			this.hostsToScan = new HashSet<>(Arrays.asList(serverHost.split(";")));
		}
		this.clientHost = HttpHelper.getLocalHost();
		// if (serverHost.indexOf(":") > 0) {
		// System.out.println(serverHost.substring(serverHost.lastIndexOf(":") +
		// 1).replaceAll("\\/", ""));
		// this.serverPort = Integer
		// .parseInt(serverHost.substring(serverHost.lastIndexOf(":") +
		// 1).replaceAll("\\/", ""));
		// } else if (serverHost.startsWith("https")) {
		// this.serverPort = 443;
		// } else if (serverHost.startsWith("http")) {
		// this.serverPort = 80;
		// }
		logDirectory = properties.getProperty("server_log_directory",
				Paths.get("").toAbsolutePath().toString() + "\\logs\\");

		this.serverVersion = properties.getProperty("server_version_no", "Server-1");

		this.readRestroomUrl = properties.getProperty("read-restroom-url", "tableservice/restrooms");// http://pserver:8080/restroom");
		this.pingUrl = properties.getProperty("ping-url", "tableservice/ping");// "http://pserver:8080/ping");
		this.readStaffNumberChangeUrl = properties.getProperty("staff-number-change", "tableservice/staffs/counts");
		// "http://pserver:8080/staff_number_change");
		this.pushNotificationUrl = properties.getProperty("push-notification-url", "tableservice/tables");
		// "http://pserver:8080/pushnotification");
		this.writeRestroomUrl = properties.getProperty("write-restroom-url", "tableservice/restrooms");// "http://pserver:8080/restroom");
		this.readManagerUrl = properties.getProperty("read-manager-url", "tableservice/managers/statuses");// "http://pserver:8080/manager");
		this.writeManagerUrl = properties.getProperty("write-manager-url", "tableservice/managers/statuses");// "http://pserver:8080/manager");
		this.readUrl = properties.getProperty("read-url", "tableservice/tables");// "http://pserver/cgi-bin/reader_s.pl?cfg=seatme.cfg");
		this.writeUrl = properties.getProperty("write-url", "tableservice/tables");// "http://pserver/cgi-bin/writer_s.pl?cfg=seatme.cfg");
		this.staffListUrl = properties.getProperty("staff-list-url", "tableservice/staffs");// "http://pserver/staff.php");
		this.staffSectionUrl = properties.getProperty("staff-section-url", "tableservice/staffs/sections");// "http://pserver:8080/staffsection");
		this.staffUrl = properties.getProperty("staff-url", "tableservice/staffs");// "http://pserver/staff");
		this.staffLogUrl = properties.getProperty("staff-log-url", "tableservice/stafflogs");// "http://pserver/staff.php");
		this.sectionUrl = properties.getProperty("section-url", "http://pserver:8080/section");
		this.menuOptionUrl = properties.getProperty("menu-option-url", "tableservice/menus");// "http://pserver:8080/menus");
		this.lockUrl = properties.getProperty("menu-lock-url", "tableservice/menus/lock");// "http://pserver:8080/menus/lock");
		this.sectionCountUrl = properties.getProperty("sections-count-url", "tableservice/sections/counts");// "http://pserver:8080/sections/count");
		this.serverShutUrl = properties.getProperty("server-shut-url", "tableservice/shut");// "http://pserver/staff.php");
		this.password = properties.getProperty("password", "tablecheck");
		this.sections = Integer.parseInt(properties.getProperty("sections", "7"));
		this.zoomFactor = Double.parseDouble(properties.getProperty("zoom-factor", "0.5"));
		this.layout = properties.getProperty("layout", "default.json");
		this.layoutFolder = properties.getProperty("layout_directory", "layouts");
		this.showKeyboard = Boolean.parseBoolean(properties.getProperty("show-keyboard", "true"));
		this.useTabtip = Boolean.parseBoolean(properties.getProperty("use-tabtip", "true"));
		this.logMessage = Boolean.parseBoolean(properties.getProperty("log-messages", "false"));
		this.logsRetainDays = Integer.parseInt(properties.getProperty("logs_retain_days", "90"));
		this.logsDayHours = Integer.parseInt(properties.getProperty("logs_day_hour", "0"));

		this.email_from = properties.getProperty("email_from", "noreply.tablecheck@gmail.com");
		this.email_sub = properties.getProperty("email_sub", "Subject of mail");
		this.email_content = properties.getProperty("email_content", "Main content of email");
		this.email_smtp_host = properties.getProperty("email_smtp_host", "smtp.gmail.com");
		this.email_smtp_port = Integer.parseInt(properties.getProperty("email_smtp_port", "0"));
		this.email_protocol = properties.getProperty("email_protocol", "smtp");
		this.email_tls_enabled = Boolean.parseBoolean(properties.getProperty("email_tls_enabled", "false"));
		this.email_smtp_username = properties.getProperty("email_smtp_username", "demo.tablecheck@gmail.com");
		this.email_smtp_password = properties.getProperty("email_smtp_password", "");

		this.panelWriteUrl = properties.getProperty("panel-write-url", "http://localhost:8080/table");
		this.hideResets = Boolean.parseBoolean(properties.getProperty("reset-buttons-hide", "false"));
		this.broadcastPort = Integer.parseInt(properties.getProperty("broadcast_port", "8445"));
                //Updated by Han - change broadcast port as not useful port
                //this.broadcastPort = Integer.parseInt(properties.getProperty("broadcast_port", "4446"));
		this.broadcastServerHost = properties.getProperty("broadcast_server_host", "230.0.0.0");//"255.255.255.255");

		servers = new HashSet<>();
		servers.addAll(Arrays.asList(properties.getProperty("server-list", "").split(";")));
		if (!new File(logDirectory).exists()) {
                    new File(logDirectory).mkdirs();
		}
	}

	private static Config instance;
	private Properties prop;

	public static void loadProperty(Properties prop) {
            instance = new Config(prop);
            instance.prop = prop;
	}

	public static Config getInstance() {
		return instance;
	}

	public boolean isServer() {
		return this.server;
	}

	public void setServer(boolean server) {
		this.server = server;
	}

	public boolean isClient() {
		return this.client;
	}

	public void setClient(boolean client) {
		this.client = client;
	}

	public String getServerHost() {
		return this.serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public int getServerPort() {
		return this.serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
        
        public int getRThreadStatus() {
		return this.nRThreadStatus;
	}

	public void setRThreadStatus(int status) {
		this.nRThreadStatus = status;
	}
        
        public long getLThreadTime() {
		return this.nLThreadTime;
	}

	public void setLThreadTime(long ctime) {
		this.nLThreadTime = ctime;
	}
        
        public int getBThreadStatus() {
		return this.nBThreadStatus;
	}

	public void setBThreadStatus(int status) {
		this.nBThreadStatus = status;
	}

	public String getMenuOptionUrl() {
		return menuOptionUrl;
	}

	public void setMenuOptionUrl(String menuOptionUrl) {
		this.menuOptionUrl = menuOptionUrl;
	}

	public String getLockUrl() {
		return lockUrl;
	}

	public void setLockUrl(String lockUrl) {
		this.lockUrl = lockUrl;
	}

	public String getSectionCountUrl() {
		return sectionCountUrl;
	}

	public void setSectionCountUrl(String sectionCountUrl) {
		this.sectionCountUrl = sectionCountUrl;
	}

	public int getLogsRetainDays() {
		return logsRetainDays;
	}

	public void setLogsRetainDays(int logsRetainDays) {
		this.logsRetainDays = logsRetainDays;
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}

	public String getEmail_from() {
		return email_from;
	}

	public void setEmail_from(String email_from) {
		this.email_from = email_from;
	}

	public String getEmail_sub() {
		return email_sub;
	}

	public void setEmail_sub(String email_sub) {
		this.email_sub = email_sub;
	}

	public String getEmail_content() {
		return email_content;
	}

	public void setEmail_content(String email_content) {
		this.email_content = email_content;
	}

	public String getEmail_smtp_host() {
		return email_smtp_host;
	}

	public void setEmail_smtp_host(String email_smtp_host) {
		this.email_smtp_host = email_smtp_host;
	}

	public int getEmail_smtp_port() {
		return email_smtp_port;
	}

	public void setEmail_smtp_port(int email_smtp_port) {
		this.email_smtp_port = email_smtp_port;
	}

	public String getEmail_protocol() {
		return email_protocol;
	}

	public void setEmail_protocol(String email_protocol) {
		this.email_protocol = email_protocol;
	}

	public boolean isEmail_tls_enabled() {
		return email_tls_enabled;
	}

	public void setEmail_tls_enabled(boolean email_tls_enabled) {
		this.email_tls_enabled = email_tls_enabled;
	}

	public String getEmail_smtp_username() {
		return email_smtp_username;
	}

	public void setEmail_smtp_username(String email_smtp_username) {
		this.email_smtp_username = email_smtp_username;
	}

	public String getEmail_smtp_password() {
		return email_smtp_password;
	}

	public void setEmail_smtp_password(String email_smtp_password) {
		this.email_smtp_password = email_smtp_password;
	}

	public int getLogsDayHours() {
		return logsDayHours;
	}

	public void setLogsDayHours(int logsDayHours) {
		this.logsDayHours = logsDayHours;
	}

	public String getClientHost() {
		return clientHost;
	}

	public void setClientHost(String clientHost) {
		this.clientHost = clientHost;
	}
        
        public boolean getSwipeStatus() {
		return bSwipeStatus;
	}

	public void setSwipeStatus(boolean SwipeStatus) {
		this.bSwipeStatus = SwipeStatus;
	}

	public String getLogDirectory() {
		return logDirectory;
	}

	public void setLogDirectory(String logDirectory) {
		this.logDirectory = logDirectory;
	}

	public boolean isServerChanging() {
		return ServerChanging;
	}

	public void setServerChanging(boolean ServerChanging) {
		this.ServerChanging = ServerChanging;
	}

	public Set<String> getHostsToScan() {
		if (hostsToScan == null) {
			hostsToScan = new HashSet<>();
		}
		return hostsToScan;
	}

	public void setHostsToScan(Set<String> hostsToScan) {
		this.hostsToScan = hostsToScan;
	}

	private boolean movingTables;

	public boolean isMovingTables() {
		return movingTables;
	}

	public void setMovingTables(boolean movingTables) {
		this.movingTables = movingTables;
	}

	private double layoutLeftAdjusted;
	private double layoutTopAdjusted;

	public double getLayoutLeftAdjusted() {
		return layoutLeftAdjusted;
	}

	public void setLayoutLeftAdjusted(double layoutLeftAdjusted) {
		this.layoutLeftAdjusted = layoutLeftAdjusted;
	}

	public double getLayoutTopAdjusted() {
		return layoutTopAdjusted;
	}

	public void setLayoutTopAdjusted(double layoutTopAdjusted) {
		this.layoutTopAdjusted = layoutTopAdjusted;
	}

	public String getLayoutFolder() {
		return layoutFolder;
	}

	public void setLayoutFolder(String layoutFolder) {
		this.layoutFolder = layoutFolder;
	}

	public String getPanelWriteUrl() {
		return panelWriteUrl;
	}

	public void setPanelWriteUrl(String panelWriteUrl) {
		this.panelWriteUrl = panelWriteUrl;
	}

	public boolean isHideResets() {
		return hideResets;
	}

	public Set<String> getServers() {
		return servers;
	}

	public String getServerList() {
		String s1 = "";
		for (String s : servers) {
                    if (s!=null)
                        s1 += (s1.length() > 0 ? ";" : "") + s;
		}
		return s1;
	}

	public void addServers(String server) {
		if (this.servers == null)
			this.servers = new HashSet<>();
		this.servers.add(server);
	}

	public void saveConfig(boolean isServer, String status) {
            if(isServer) {
                instance.prop.setProperty("system_as_server", "" + server);
            }
            instance.prop.setProperty("server_host", "" + clientHost);
            instance.prop.setProperty("server-list", "" + getServerList());
            instance.prop.setProperty("status", "" + status);
            instance.prop.setProperty("swipe_status", "" + bSwipeStatus);
            
            OutputStream oStream = null;
            try {
                oStream = new FileOutputStream(CONFIG_FILE);
                prop.store(oStream, null);
            } catch (Exception ex) {
                    ex.printStackTrace();
            } finally {
                if (oStream != null)
                try {
                        oStream.close();
                } catch (Exception ex) {
                        ex.printStackTrace();
                }
            }
	}

	public int getBroadcastPort() {
		return broadcastPort;
	}

	public void setBroadcastPort(int broadcastPort) {
		this.broadcastPort = broadcastPort;
	}

	public boolean isServerConnected() {
		return serverConnected;
	}

	public void setServerConnected(boolean serverConnected) {
		this.serverConnected = serverConnected;
	}

	public String getBroadcastServerHost() {
		return broadcastServerHost;
	}

	public void setBroadcastServerHost(String broadcastServerHost) {
		this.broadcastServerHost = broadcastServerHost;
	}

}
