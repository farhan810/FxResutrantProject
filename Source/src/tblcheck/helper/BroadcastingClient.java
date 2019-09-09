package tblcheck.helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import tblcheck.model.Config;

public class BroadcastingClient {
	private DatagramSocket socket;
//	private InetAddress address;
	private int expectedServerCount;
	private byte[] buf;

	public BroadcastingClient(int expectedServerCount) throws Exception {
		this.expectedServerCount = expectedServerCount;
//		this.address = InetAddress.getByName("255.255.255.255");
	}

	public static void main(String[] args) throws IOException {
	
		System.out.println("broadcasting client here farhan BroadcastingClient");
		BroadcastingClient cl;
		try {
			cl = new BroadcastingClient(1);
			cl.discoverServers(Config.getInstance().getClientHost());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int discoverServers(String msg) throws IOException {
		initializeSocketForBroadcasting();
		copyMessageOnBuffer(msg);

		// When we want to broadcast not just to local network, call
		// listAllBroadcastAddresses() and execute broadcastPacket for each
		// value.
		List<InetAddress> lst = listAllBroadcastAddresses();
		for (InetAddress i : lst) {
//			address = i;
			broadcastPacket(i);
		}

		return receivePackets();
	}

	List<InetAddress> listAllBroadcastAddresses() throws SocketException {
		List<InetAddress> broadcastList = new ArrayList<>();
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		System.out.println(interfaces);
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();
			System.out.println(networkInterface.getName());
			if (networkInterface.isLoopback() || !networkInterface.isUp()) {
				continue;
			}

			broadcastList.addAll(
					networkInterface.getInterfaceAddresses().stream().filter(address -> address.getBroadcast() != null)
							.map(address -> address.getBroadcast()).collect(Collectors.toList()));
		}
		System.out.println(broadcastList);
		return broadcastList;
	}

	private void initializeSocketForBroadcasting() throws SocketException {
		socket = new DatagramSocket();
		socket.setBroadcast(true);
	}

	private void copyMessageOnBuffer(String msg) {
		buf = msg.getBytes();
	}

	private void broadcastPacket(InetAddress address) throws IOException {
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Config.getInstance().getBroadcastPort());
		socket.send(packet);
	}

	private int receivePackets() throws IOException {
		int serversDiscovered = 0;
		while (serversDiscovered != expectedServerCount) {
			receivePacket();
			serversDiscovered++;
		}
		return serversDiscovered;
	}

	private void receivePacket() throws IOException {
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
	}

	public void close() {
		socket.close();
	}
}
