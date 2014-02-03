package ar.unlp.info.laboratorio.javaClickers.network.operations;

import java.io.IOException;
import java.net.Socket;

import ar.unlp.info.laboratorio.javaClickers.auxiliary.Par;
import ar.unlp.info.laboratorio.javaClickers.model.Information;
import ar.unlp.info.laboratorio.javaClickers.network.Manager;
import ar.unlp.info.laboratorio.javaClickers.network.com.Receiver;
import ar.unlp.info.laboratorio.javaClickers.network.com.Sender;

/**
 * Created by Jony on 02/06/13.
 */
public class InformationOperation extends Operation {

    public static final long serialVersionUID = 89L;

    @Override
    public void executeOnClient() {
        try {
            Manager.getInstance().setInformation(this.getInformation());
            super.executeOnClient();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Information getInformation() throws IOException, ClassNotFoundException {
        return ((ResultOperation<Information>)
                Receiver.receiveTCP(
                        Sender.sendTCP(
                                new Par<Socket, Operation>(
                                        new Socket(Manager.getInstance().getServer().getAddress(), Manager.getInstance().getServer().getPort()), this)
                        )).getValue()).getResult();
    }

    @Override
    public void executeOnServer(Serviceable serverInterface) {
        try {
            System.out.println("Information request received");
            Sender.sendTCP(
                    new Par<Socket, Operation>(serverInterface.getSocket(), new ResultOperation<Information>(Manager.getInstance().getInformation())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
