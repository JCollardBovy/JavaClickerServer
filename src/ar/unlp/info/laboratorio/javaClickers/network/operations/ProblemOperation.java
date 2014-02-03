package ar.unlp.info.laboratorio.javaClickers.network.operations;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import ar.unlp.info.laboratorio.javaClickers.auxiliary.Par;
import ar.unlp.info.laboratorio.javaClickers.model.Problem;
import ar.unlp.info.laboratorio.javaClickers.network.Manager;
import ar.unlp.info.laboratorio.javaClickers.network.com.Receiver;
import ar.unlp.info.laboratorio.javaClickers.network.com.Sender;

/**
 * Created by Jony on 05/07/13.
 */
public class ProblemOperation extends Operation {

    public static final long serialVersionUID = 90L;

    @Override
    public void executeOnClient() {
        try {
            Problem problem = null;
            while (problem == null){
                Par<Socket, Operation> receivedPair = Receiver.receiveTCP(Sender.sendTCP(new Par<Socket, Operation>(new Socket(Manager.getInstance().getServer().getAddress(), Manager.getInstance().getServer().getPort()), this)));
                receivedPair.getKey().close();
                problem = ((ResultOperation<Problem>)receivedPair.getValue()).getResult();
                if (problem == null){
                    (Receiver.receiveUDP(new Par<DatagramSocket, DatagramPacket>(new DatagramSocket(Manager.clientBroadcastPort), new DatagramPacket(new byte[1024], 1024)))).getKey().close();
                }
            }
            Manager.getInstance().setProblem(problem);
            super.executeOnClient();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeOnServer(Serviceable serverInterface) {
        try {
            System.out.println("Problem request received");
            Sender.sendTCP(
                    new Par<Socket, Operation>(serverInterface.getSocket(), new ResultOperation<Problem>(Manager.getInstance().getProblem())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
