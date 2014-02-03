package ar.unlp.info.laboratorio.javaClickers.network.operations;

/**
 * Created by Jony on 03/06/13.
 */
public class ResultOperation<E> extends Operation {

    public static final long serialVersionUID = 91L;

    private E result;

    public ResultOperation(E result) {
        this.result = result;
    }

    private ResultOperation() {
    }

    public E getResult(){
        return result;
    }

    @Override
    public void executeOnClient() {
        super.executeOnClient();
    }

    @Override
    public void executeOnServer(Serviceable serverInterface) {
    }
}
