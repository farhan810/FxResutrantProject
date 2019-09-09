package tblcheck.helper;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import tblcheck.view.MessageConstants;

public class ClientHelper {

	public static void restartWindows(String resetType) {
		try {
			Runtime.getRuntime().exec("shutdown -r -f");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void restartApplication(String resetType, Runnable runBeforeRestart) throws IOException {

		try {
			if (resetType == null || MessageConstants.RESET_TYPE_PERM.equals(resetType)) {
				Thread.sleep(1000);
				restartWindows(resetType);
			}
			Thread.sleep(3000);

			String java = System.getProperty("java.home") + "/bin/java";
			List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
			StringBuffer vmArgsOneLine = new StringBuffer();
			for (String arg : vmArguments) {
				if (!arg.contains("-agentlib")) {
					vmArgsOneLine.append(arg);
					vmArgsOneLine.append(" ");
				}
			}
			final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);
			String[] mainCommand = System.getProperty("sun.java.command").split(" ");
			if (mainCommand[0].endsWith(".jar")) {
				cmd.append("-jar " + new File(mainCommand[0]).getPath());
			} else {
				cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
			}
			for (int i = 1; i < mainCommand.length; i++) {
				cmd.append(" ");
				cmd.append(mainCommand[i]);
			}
			cmd.append(" TEMPORARY > runserver_temp.log");

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						if (MessageConstants.RESET_TYPE_TEMP.equals(resetType)) {
							Thread.sleep(5000);
						} else if (resetType != null) {
							Thread.sleep(2000);
						}
						System.out.println(cmd.toString());
						Runtime.getRuntime().exec(cmd.toString());
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			if (runBeforeRestart != null) {
				runBeforeRestart.run();
			}
			System.exit(0);
		} catch (Exception e) {
			throw new IOException("Error while trying to restart the application", e);
		}
	}

	// public static void main(String[] args) {
	// List host = new ArrayList<>();
	// System.out.println(scan(host, 8080, 1));
	// System.out.println(host);
	// }

	public static String scan(List<String> hosts, int port, int t) {

		String server = null;
		System.out.println("Searching for a Wii...");

		InetAddress localhost = null;

		try {
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		byte[] ip = localhost.getAddress();

		for (int i = 1; i <= 3; i++) {
			try {
				ip[3] = (byte) i;
				InetAddress address = InetAddress.getByAddress(ip);

				if (address.isReachable(10 * t)) {
					String output = address.toString().substring(1);
					try (Socket socket = new Socket(output, port)) {
                                            socket.close();
                                            System.out.println("and is potentially a Wii!");
                                            server = output;
					} catch (Exception e) {
                                            System.out.println();
					}
					hosts.add(output);
				}
			} catch (ConnectException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return server;
	}
}
