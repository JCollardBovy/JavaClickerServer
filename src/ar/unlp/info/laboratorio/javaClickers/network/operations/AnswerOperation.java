package ar.unlp.info.laboratorio.javaClickers.network.operations;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;

import ar.unlp.info.laboratorio.javaClickers.auxiliary.Par;
import ar.unlp.info.laboratorio.javaClickers.model.Solution;
import ar.unlp.info.laboratorio.javaClickers.network.Manager;
import ar.unlp.info.laboratorio.javaClickers.network.com.Receiver;
import ar.unlp.info.laboratorio.javaClickers.network.com.Sender;

/**
 * Created by Jony on 09/07/13.
 */
public class AnswerOperation extends Operation {

    public static final long serialVersionUID = 93L;

    Solution aSolution;

    public AnswerOperation(Solution aSolution) {
        this.aSolution = aSolution;
    }

    @Override
    public void executeOnClient() {
        try {
            Socket socket = new Socket(Manager.getInstance().getServer().getAddress(), Manager.getInstance().getServer().getPort());
//            socket.setSoTimeout(3000);
            ((ResultOperation<Boolean>)
                Receiver.receiveTCP(
                    Sender.sendTCP(
                        new Par<Socket, Operation>(socket, this)
                    )).getValue()).getResult();
            super.executeOnClient();
        } catch (InterruptedIOException e) {
            this.executeOnClient();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeOnServer(Serviceable serverInterface) {
        try {
            System.out.println("new Solution:" + aSolution.toString());
            Manager.getInstance().newSolution(this.aSolution);
            Sender.sendTCP(
                    new Par<Socket, Operation>(serverInterface.getSocket(), new ResultOperation<Boolean>(true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
