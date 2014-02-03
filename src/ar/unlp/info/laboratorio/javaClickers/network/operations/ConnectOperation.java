package ar.unlp.info.laboratorio.javaClickers.network.operations;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import ar.unlp.info.laboratorio.javaClickers.auxiliary.Par;
import ar.unlp.info.laboratorio.javaClickers.network.Manager;
import ar.unlp.info.laboratorio.javaClickers.network.com.Receiver;
import ar.unlp.info.laboratorio.javaClickers.network.com.Sender;


/**
 * Created by Jony on 02/06/13.
 */
public class ConnectOperation extends Operation {

    public static final long serialVersionUID = 88L;

    @Override
    public void executeOnClient() {
        try {
            Manager.getInstance().setServer(this.discover());
            super.executeOnClient();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeOnServer(Serviceable serverInterface) {
        try {
            Par<DatagramSocket, DatagramPacket> comInfo = new Par<DatagramSocket, DatagramPacket>(new DatagramSocket(Manager.serverBroadcastPort), new DatagramPacket(new byte[1024], 1024));
            while (true){
                Receiver.receiveUDP(comInfo).getValue().setData(ByteBuffer.allocate(4).putInt(Manager.getInstance().getServer().getPort()).array());
                System.out.println("Received a new Client");
                System.out.println(comInfo.getValue().getData());
                Sender.sendUDP(comInfo);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private InetSocketAddress discover() throws SocketException {
        DatagramPacket packet = (Receiver.receiveUDP(
                                    Sender.sendBroadcast(
                                        new Par<DatagramSocket, DatagramPacket>(
                                                new DatagramSocket(), new DatagramPacket(new byte[1024], 1024, null, Manager.serverBroadcastPort))
                                    ))).getValue();
        return new InetSocketAddress(packet.getAddress(), ByteBuffer.wrap(packet.getData()).getInt());
    }

}
