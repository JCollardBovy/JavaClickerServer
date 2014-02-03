package ar.unlp.info.laboratorio.javaClickers.network;

import ar.unlp.info.laboratorio.javaClickers.TimerAction;
import ar.unlp.info.laboratorio.javaClickers.model.Information;
import ar.unlp.info.laboratorio.javaClickers.model.Problem;
import ar.unlp.info.laboratorio.javaClickers.model.Solution;

import java.net.InetSocketAddress;
import java.util.Observable;

/**
 * Facade Pattern
 * Created by Jony on 23/05/13.
 */
public abstract class Manager extends Observable{

    public static int serverBroadcastPort = 45678;
    public static int clientBroadcastPort = 45680;

    /**
     * Unica instancia de ClientManager, implementacion del patron Singleton.
     */
    protected static Manager instance;
    /**
     *
     */
    private Information information;
    /**
     * Informacion de la conexion actual al servidor.
     */
    private InetSocketAddress server;
    private Problem problem;
    /**
     * Conector para las operaciones de red
     */
    private Operator operator;

    /**
     * Invalida el constructor para no generar nuevas instancias.
     */
    protected Manager() {
    }

    public static synchronized Manager getInstance() {
        return instance;
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public InetSocketAddress getServer() {
        return server;
    }

    public void setServer(InetSocketAddress server) {
        this.server = server;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    protected Operator getOperator() {
        return operator;
    }

    protected void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void startTimer(final TimerAction timerAction) {
        getOperator().startTimer(getProblem().getTime(), timerAction);
    }

    public abstract void newSolution(Solution aSolution);

    public abstract void disconnect(Object aDisconnectParam);
}
