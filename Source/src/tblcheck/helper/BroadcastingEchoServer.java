package tblcheck.helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import tblcheck.model.Config;

public class BroadcastingEchoServer extends Thread {

	protected DatagramSocket socket = null;
	protected boolean running;
	protected byte[] buf = new byte[256];

	public BroadcastingEchoServer() throws IOException {
		socket = new DatagramSocket(null);
		socket.setReuseAddress(true);
		socket.bind(new InetSocketAddress(Config.getInstance().getBroadcastPort()));
	}

	public void run() {
		running = true;

		while (!Config.getInstance().isServerConnected()) {
			try {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				packet = new DatagramPacket(buf, buf.length, address, port);
				String received = new String(packet.getData(), 0, packet.getLength());
				if (received.equals("end")) {
					running = false;
					continue;
				} else if (received != null && received.trim().length() > 0) {
					try {
						InetAddress address1 = InetAddress.getByName(received);
						if (address1.isReachable(2)) {
							try (Socket socket = new Socket(received, port)) {
                                                                socket.close();
								System.out.println("Server Found");
								Config.getInstance().setServerHost(received);
								Config.getInstance().setServerChanging(false);
								Config.getInstance().addServers(received);
								Config.getInstance().saveConfig(false, "BroadCastEcho");
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}
						}
					} catch (Exception e) {
						System.out.println(e.getMessage() +" >> "+ received);
					}
				}
//				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
			}
		}
		socket.close();
	}
}
