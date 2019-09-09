package tblcheck.helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tblcheck.model.Config;


public class MulticastPublisher implements Runnable {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;

    public void multicast(InetAddress source,String multicastMessage) throws IOException {
        socket = new DatagramSocket(0,source);
        group = InetAddress.getByName(Config.getInstance().getBroadcastServerHost());
        buf = multicastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, Config.getInstance().getBroadcastPort());
        socket.send(packet);
        socket.close();
    }

    @Override
    public void run() {
        System.out.println("=== Broadcast Thread started!");
        int nTry=0; //keep server status till nTry<3 - wait 2 seconds;
        Config.getInstance().setBThreadStatus(0);
        while (Config.getInstance().getBThreadStatus()==0 && (Config.getInstance().isServer() || nTry<3) && !Config.getInstance().isServerChanging()) {
            if (nTry<3) 
            {
                Config.getInstance().setServer(true);
                nTry++;
            }
            try {
                String localhost = HttpHelper.getLocalHost();
                InetAddress sourceAddress = InetAddress.getByName(localhost);
                if(sourceAddress.isLoopbackAddress())
                    System.out.println("Source address is a loopback interface");
                else if(sourceAddress.getHostAddress().startsWith("169.254."))
                    System.out.println("Source address is an automatic private address");
                else{
                    System.out.println("Broadcasting \""+localhost+"\" ntry:"+nTry);
                    multicast(sourceAddress,localhost);
                    Config.getInstance().setServerHost(localhost);
                    Config.getInstance().setServerConnected(true);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Config.getInstance().setBThreadStatus(2);
        System.out.println("=== Broadcast Thread - stoped ===");
    }
    
//	private InetAddress group;
//        private InetAddress previousMyIP=null;
//	private byte[] buf;
//
//        
//        @Override
//        public void run() {
//            MulticastSocket socket = null;
//            InetAddress group = null;
//            try {
//                String localhost = HttpHelper.getLocalHost();
//                InetAddress sourceAddress = InetAddress.getByName(localhost);
//
//                if(sourceAddress.isLoopbackAddress())
//                    System.out.println("Source address is a loopback interface");
//                else if(sourceAddress.getHostAddress().startsWith("169.254."))
//                    System.out.println("Source address is an automatic private address");
//                else{
//                    System.out.println("Broadcasting \""+localhost+"\"");
//                    previousMyIP = sourceAddress;
//                    
//                    socket.setReuseAddress(true);
//                    socket.setInterface(sourceAddress);
//                    group = InetAddress.getByName(Config.getInstance().getBroadcastServerHost());
//                    socket.joinGroup(group);
//                }
//
//                BroadcastMessage(socket);
//                
//                socket.leaveGroup(group);
//                socket.close();
//
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        
//	private void BroadcastMessage(MulticastSocket socket) {
//            while (Config.getInstance().isServer() && !Config.getInstance().isServerChanging() && Config.getInstance().isServerConnected()) {
//                try {
//                    String localhost = HttpHelper.getLocalHost();
//                    InetAddress sourceAddress = InetAddress.getByName(localhost);
//
//                    if(sourceAddress.isLoopbackAddress())
//                        System.out.println("Source address is a loopback interface");
//                    else if(sourceAddress.getHostAddress().startsWith("169.254."))
//                        System.out.println("Source address is an automatic private address");
//                    else{
//                        System.out.println("---> Broadcasting ip: "+localhost);
//                        if (!sourceAddress.equals(previousMyIP))
//                        {
//                            socket.setInterface(sourceAddress);
//                            previousMyIP = sourceAddress;
//                        }
//                        buf = localhost.getBytes();
//                        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, Config.getInstance().getBroadcastPort());
//                        socket.send(packet);
//                    }
//                    Thread.sleep(1000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//	}
}