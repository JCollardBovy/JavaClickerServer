package ar.unlp.info.laboratorio.javaClickers.network.com;

import ar.unlp.info.laboratorio.javaClickers.auxiliary.Par;
import ar.unlp.info.laboratorio.javaClickers.network.operations.Operation;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 01/06/13
 * Time: 21:13
 * To change this template use File | Settings | File Templates.
 */
public class Sender{

    public static Par<DatagramSocket, DatagramPacket> sendUDP(Par<DatagramSocket, DatagramPacket> comInfo){
        try {
            comInfo.getKey().send(comInfo.getValue());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return comInfo;
    }

    public static Par<DatagramSocket, DatagramPacket> sendBroadcast(Par<DatagramSocket, DatagramPacket> comInfo){
        Iterator<InetAddress> iterator = Sender.getBroadcastIp().iterator();
        while (iterator.hasNext()){
            comInfo.getValue().setAddress(iterator.next());
            Sender.sendUDP(comInfo);
        }
        return comInfo;
    }

    public static Par<Socket, Operation> sendTCP(Par<Socket, Operation> comInfo) throws IOException {
        ObjectOutputStream output = new ObjectOutputStream(comInfo.getKey().getOutputStream());
        output.writeObject(comInfo.getValue());
        output.flush();
        return comInfo;
    }

    private static List<InetAddress> getBroadcastIp() {
        List<InetAddress> broadcastAddress = new LinkedList<InetAddress>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                Iterator<InterfaceAddress> iterator = en.nextElement().getInterfaceAddresses().iterator();
                while(iterator.hasNext()){
                    InetAddress address = iterator.next().getBroadcast();
                    if (address!= null){
                        broadcastAddress.add(address);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return broadcastAddress;
    }

}
