package ar.unlp.info.laboratorio.javaClickers.network.operations;

import java.io.Serializable;

/**
 * Created by Jony on 02/06/13.
 */
public abstract class Operation implements Serializable{

    public static final long serialVersionUID = 87L;

    private boolean finished = false;

    public void executeOnClient(){
        this.finished = true;
    }

    public abstract void executeOnServer(Serviceable serverInterface);

    public boolean isFinished() {
        return finished;
    }
}
