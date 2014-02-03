package ar.unlp.info.laboratorio.javaClickers.network.operations;

import java.io.IOException;
import java.net.Socket;

import ar.unlp.info.laboratorio.javaClickers.auxiliary.Par;
import ar.unlp.info.laboratorio.javaClickers.network.Manager;
import ar.unlp.info.laboratorio.javaClickers.network.com.Receiver;
import ar.unlp.info.laboratorio.javaClickers.network.com.Sender;

/**
 * Created by Jony on 08/09/13.
 */
public class DisconnectOperation extends Operation {

    Integer port;

    public DisconnectOperation(Integer port) {
        this.port = port;
    }

    @Override
    public void executeOnClient() {
        try {
            Receiver.receiveTCP(
                    Sender.sendTCP(
                            new Par<Socket, Operation>(
                                    new Socket(Manager.getInstance().getServer().getAddress(), Manager.getInstance().getServer().getPort()), this)
                    ));
            super.executeOnClient();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeOnServer(Serviceable serverInterface) {
        Manager.getInstance().disconnect(this.port);
        try {
            Sender.sendTCP(
                    new Par<Socket, Operation>(serverInterface.getSocket(), new ResultOperation<Boolean>(true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
