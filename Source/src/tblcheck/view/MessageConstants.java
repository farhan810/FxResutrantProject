package tblcheck.view;

public class MessageConstants {

	public static final String CONFIRM_RESET_CLIENT = "Should this system become the SMS server?\r\n\r\n"
			+ "Choose 'Yes' to make this system the server for only this time\r\n"
			+ "Choose 'Save/Reset' to permanently make this system the server\r\n"
			+ "Choose 'Reset' to restart this client only\r\n" + "Choose 'No' will exit this menu";
	public static final String CONFIRM_RESET_CLIENT_HIDE = "Should this system become the SMS server?\r\n\r\n"
			+ "Choose 'Reset' to restart this client only\r\n" + "Choose 'No' will exit this menu";
	public static final String CONFIRM_RESET_SERVER = "Should the SMS server be restarted?\r\n\r\n"
			+ "Current Server will shutdown/restart on selecting 'Yes'\r\n" + "Selecting 'No' will exit this menu";

	public static final String RESET_TYPE_PERM = "PERMANENT";
	public static final String RESET_TYPE_TEMP = "TEMPORARY";
}
