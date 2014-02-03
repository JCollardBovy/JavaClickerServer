package ar.unlp.info.laboratorio.javaClickers.network.operations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 06/07/13
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class WorkerOperation extends Operation {

    public static final long serialVersionUID = 92L;

    ServerSocket socket;
    Socket currentClientSocket;

    public WorkerOperation(ServerSocket socket) {
        this.socket = socket;
    }

    @Override
    public void executeOnClient() {
        super.executeOnClient();
    }

    @Override
    public void executeOnServer(Serviceable serverInterface) {
        while (true){
            try {
                currentClientSocket = socket.accept();
                System.out.println("TCP received");
                ((Operation)(new ObjectInputStream(currentClientSocket.getInputStream())).readObject()).executeOnServer(new Serviceable() {
                    @Override
                    public Socket getSocket() {
                        return currentClientSocket;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static ServerSocket getNewSocket(){
        ServerSocket socket = null;
        while (socket == null){
            try {
                socket = new ServerSocket((new Random()).nextInt(40001) + 20000);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return socket;
    }
}
