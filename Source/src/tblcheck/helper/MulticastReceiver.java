package tblcheck.helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import tblcheck.model.Config;

public class MulticastReceiver extends Thread {

        private static Pattern ipAddressPattern;
	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        
        
       	public void run() {
            System.out.println("=== MulticastReceiver Started! Thread ID is "+this.getId());
            ipAddressPattern = Pattern.compile(IPADDRESS_PATTERN);
            Config.getInstance().setRThreadStatus(0);
            while (Config.getInstance().getRThreadStatus()==0) {
                try {
                    String localhost;
                    InetAddress address;
                    do {
                        Thread.sleep(3000);
                        localhost = HttpHelper.getLocalHost();
                        address = InetAddress.getByName(localhost);
                    } while (address.isLoopbackAddress() ||  localhost.equals("localhost") ||  localhost.equals("127.0.0.1"));
                    
                    runCasting();
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    
                } catch (UnknownHostException ex) {
                    Logger.getLogger(MulticastReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MulticastReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
            Config.getInstance().setRThreadStatus(2);
            System.out.println("=== MulticastReceiver - stoped");
	}

	public void runCasting() {
		try {
                    if (!Config.getInstance().isServerChanging()) {
                        String localhost = HttpHelper.getLocalHost();
                        InetAddress address = InetAddress.getByName(localhost);
                        if(address.isLoopbackAddress()){
                            System.out.println("MulticastReceiver: Network is not available");
                            return;
                        }
			MulticastSocket socket = new MulticastSocket(Config.getInstance().getBroadcastPort());
			InetAddress group = InetAddress.getByName(Config.getInstance().getBroadcastServerHost());
			socket.joinGroup(group);
                        
                        byte[] buf = new byte[256];
                        
                        long t = System.currentTimeMillis();
                        
                        while (System.currentTimeMillis() -  t < 10) {
                            DatagramPacket packet = new DatagramPacket(buf, buf.length);
                            socket.receive(packet);
                            String received = new String(packet.getData(), 0, packet.getLength());
                            if (received.trim().length() > 0 && ipAddressPattern.matcher(received).matches()) 
                            {
                                if (Config.getInstance().isServer()==false)
                                {
                                    System.out.println("<<< MulticastReceiver: received server ip:"+received);
                                    String oldHost = "";
                                    if (Config.getInstance().getServerHost()!=null) oldHost=Config.getInstance().getServerHost();
                                    if (received.equals(oldHost)==false || Config.getInstance().isServerConnected()==false)
                                    {
                                        Config.getInstance().setServerHost(received);
                                        Config.getInstance().addServers(received);
                                        Config.getInstance().setServer(false);
                                        if (InetAddress.getByName(received).isReachable(1000)) {
                                            System.out.println(">>>>> Reached Server ip - " + received);
                                            try (Socket socket1 = new Socket(received, Config.getInstance().getServerPort())) {
                                                socket1.close();
                                                System.out.println(">>>>> Reached Server port - " + received);
                                                Config.getInstance().setServerConnected(true);                                
                                                Config.getInstance().saveConfig(false, "MulticastReceiver1");
                                            } catch (Exception e1) {
                                                Config.getInstance().setServerConnected(false);
                                                Config.getInstance().saveConfig(false, "MulticastReceiver2");
                                                Thread.sleep(1000);
                                            }                                        
                                        }
                                        else {
                                            Config.getInstance().setServerConnected(false);
                                            Config.getInstance().saveConfig(false, "MulticastReceiver3");
                                            Thread.sleep(1000);
                                        }
                                    }
                                    Thread.sleep(3000);
                                }
                                else if (Config.getInstance().getServerHost().equals(received)==false) {
                                    //Is another server is created?
                                    System.out.println("<<< Another Server broadcast received server ip:"+received);
                                    Config.getInstance().setServer(false);
                                    Config.getInstance().setServerConnected(false);
                                    Thread.sleep(1000);
                                }
                                break;
                            }
                        }
                        
			socket.leaveGroup(group);
			socket.close();
                    }
		} catch (Exception e) {
                    e.printStackTrace();
		}
	}
        
        

//	public void run() {
//            ipAddressPattern = Pattern.compile(IPADDRESS_PATTERN);
//            MulticastSocket socket = null;
//
//            try {
//                String localhost;
//                InetAddress address;
//                do {
//                    Thread.sleep(2000);
//                    localhost = HttpHelper.getLocalHost();
//                    address = InetAddress.getByName(localhost);
//                } while (address.isLoopbackAddress() ||  localhost.equals("localhost") ||  localhost.equals("127.0.0.1"));
//                //System.out.println("MulticastReceiver: Listening for broadcasts");
//                socket = new MulticastSocket(Config.getInstance().getBroadcastPort());
//                InetAddress group = InetAddress.getByName(Config.getInstance().getBroadcastServerHost());
//                socket.joinGroup(group);
//
//                runCasting(socket);                    
// 
//                socket.leaveGroup(group);
//                socket.close();
//                System.out.println(">>>>> MulticastReceiver Thread - stoped >>>");
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//	}

//	public void runCasting(final MulticastSocket socket) {
//            System.out.println(">>>>> MulticastReceiver Thread - started >>>");
//            while (!Config.getInstance().stoppingAllThreads) {
//		try {
//                    if (!Config.getInstance().isServerChanging()) {
//                        byte[] buf = new byte[256];
//                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
//                        socket.receive(packet);
//                        String received = new String(packet.getData(), 0, packet.getLength());
//                        if (received.trim().length() > 0 && ipAddressPattern.matcher(received).matches()) {
//                            if (Config.getInstance().isServer()==false)
//                            {
//                                System.out.println("<<< MulticastReceiver: received server ip:"+received);
//                                String oldHost = "";
//                                if (Config.getInstance().getServerHost()!=null) oldHost=Config.getInstance().getServerHost();
//                                if (received.equals(oldHost)==false || Config.getInstance().isServerConnected()==false)
//                                {
//                                    Config.getInstance().setServerHost(received);
//                                    Config.getInstance().addServers(received);
//                                    Config.getInstance().setServer(false);
//                                    InetAddress address = InetAddress.getByName(received);
//                                    if (address.isReachable(1000)) {
//                                        System.out.println(">>>>> Reached Server ip - " + received);
//                                        try (Socket socket1 = new Socket(received, Config.getInstance().getServerPort())) {
//                                            socket1.close();
//                                            System.out.println(">>>>> Reached Server port - " + received);
//                                            Config.getInstance().setServerConnected(true);                                
//                                            Config.getInstance().saveConfig(false, "MulticastReceiver1");
//                                        } catch (Exception e1) {
//                                            Config.getInstance().setServerConnected(false);
//                                            Config.getInstance().saveConfig(false, "MulticastReceiver2");
//                                            Thread.sleep(1000);
//                                        }                                        
//                                    }
//                                    else {
//                                        Config.getInstance().setServerConnected(false);
//                                        Config.getInstance().saveConfig(false, "MulticastReceiver3");
//                                        Thread.sleep(1000);
//                                    }
//                                }
//                                Thread.sleep(3000);
//                            }
//                            else if (Config.getInstance().getServerHost().equals(received)==false) {
//                                //Is another server is created?
//                                System.out.println("<<< Another Server broadcast received server ip:"+received);
//                                Config.getInstance().setServer(false);
//                                Config.getInstance().setServerConnected(false);
//                                Thread.sleep(1000);
//                            }
//                        }
//                    }
//                    Thread.sleep(1000);
//		} catch (Exception e) {
//                    Config.getInstance().setServerConnected(false);
//                    e.printStackTrace();
//		}
//            }
//	}





}