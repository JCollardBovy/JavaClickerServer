package ar.unlp.info.laboratorio.javaClickers.network.com;

import ar.unlp.info.laboratorio.javaClickers.auxiliary.Par;
import ar.unlp.info.laboratorio.javaClickers.network.operations.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 01/06/13
 * Time: 21:11
 * To change this template use File | Settings | File Templates.
 */
public class Receiver{

    /**
     *
     * @param comInfo@return
     */
    public static Par<DatagramSocket, DatagramPacket> receiveUDP(Par<DatagramSocket, DatagramPacket> comInfo){
        try {
            comInfo.getKey().receive(comInfo.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comInfo;
    }

    public static Par<Socket, Operation> receiveTCP(Par<Socket, Operation> comInfo) throws IOException, ClassNotFoundException {
        comInfo.setValue((Operation)(new ObjectInputStream(comInfo.getKey().getInputStream()).readObject()));
        return comInfo;
    }
}
