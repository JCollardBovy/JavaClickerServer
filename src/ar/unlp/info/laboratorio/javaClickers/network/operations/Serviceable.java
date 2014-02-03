package ar.unlp.info.laboratorio.javaClickers.network.operations;

import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 06/07/13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public interface Serviceable {

    public static final long serialVersionUID = 93L;

    public Socket getSocket();

}
