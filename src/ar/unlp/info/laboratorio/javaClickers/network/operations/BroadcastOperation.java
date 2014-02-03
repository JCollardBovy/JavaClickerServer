package ar.unlp.info.laboratorio.javaClickers.network.operations;

import ar.unlp.info.laboratorio.javaClickers.network.Manager;
import ar.unlp.info.laboratorio.javaClickers.network.com.Sender;
import ar.unlp.info.laboratorio.javaClickers.auxiliary.Par;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 07/07/13
 * Time: 17:53
 * To change this template use File | Settings | File Templates.
 */
public class BroadcastOperation extends Operation {

    @Override
    public void executeOnServer(Serviceable serverInterface) {
        try {
            Sender.sendBroadcast(new Par<DatagramSocket, DatagramPacket>(new DatagramSocket(), new DatagramPacket(new byte[1024], 1024, null, Manager.clientBroadcastPort)));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
