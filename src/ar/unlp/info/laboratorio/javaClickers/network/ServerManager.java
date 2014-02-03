package ar.unlp.info.laboratorio.javaClickers.network;

import ar.unlp.info.laboratorio.javaClickers.TimerAction;
import ar.unlp.info.laboratorio.javaClickers.model.Problem;
import ar.unlp.info.laboratorio.javaClickers.model.RunningProblem;
import ar.unlp.info.laboratorio.javaClickers.model.Solution;
import ar.unlp.info.laboratorio.javaClickers.network.operations.BroadcastOperation;
import ar.unlp.info.laboratorio.javaClickers.network.operations.ConnectOperation;
import ar.unlp.info.laboratorio.javaClickers.network.operations.Operation;
import ar.unlp.info.laboratorio.javaClickers.network.operations.WorkerOperation;
import ar.unlp.info.laboratorio.javaClickers.auxiliary.Par;
import ar.unlp.info.laboratorio.javaClickers.FeedBackable;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 05/07/13
 * Time: 18:53
 * To change this template use File | Settings | File Templates.
 */
public class ServerManager extends Manager {

    private static final Integer maxClients = 10;

    private Map<Problem, Map<Solution, Integer>> classResults= new HashMap<Problem, Map<Solution, Integer>>();

    /**
     * Metodo Singleton que retorna la instancia de ClientManager
     * @return Retorna una instancia de ClientManager
     */
    public static synchronized ServerManager getInstance() {
        if(instance == null){
            instance = new ServerManager();
        }
        return (ServerManager) instance;
    }

    public ServerManager() {
        this.setOperator(new JavaOperator());
        this.setServer(new InetSocketAddress(Manager.clientBroadcastPort));
    }

    @Override
    public InetSocketAddress getServer() {
        this.newClient();
        System.out.println(super.getServer().getPort());
        return super.getServer();
    }

    public void listenForClients(FeedBackable clientFeedBack) {
        this.getOperator().execute(new ConnectOperation(), clientFeedBack);
    }

    Map<Integer, Integer> workers = new HashMap<Integer, Integer>();

    public void newClient(){
        Integer port = getWorkerPort();
        if (port == null){
            ServerSocket socket = WorkerOperation.getNewSocket();
            this.setServer(new InetSocketAddress(socket.getLocalPort()));
            workers.put(socket.getLocalPort(), 1);
            FeedBackable iface = new FeedBackable(){
                @Override
                public void taskStarted() {
                }

                @Override
                public void taskFinished() {
                }
            };
            Operation op = new WorkerOperation(socket);
            this.getOperator().execute(op, iface);
        }else{
            workers.put(port, workers.get(port)+1);
        }
    }

    private Integer getWorkerPort() {
        Iterator<Integer> iterator = workers.keySet().iterator();
        boolean ok = true;
        Integer port= null;
        while ((iterator.hasNext())&&ok){
            port = iterator.next();
            if (workers.get(port) < ServerManager.maxClients){
               workers.put(port, workers.get(port));
               ok=false;
            }
        }
        return port;
    }

    public void setProblem(final Problem aProblem, final TimerAction timerInterface) {

        //TODO Test
        System.out.println("SetProblem: " + aProblem.toString() + " con tiempo: " + aProblem.getTime());

        if (aProblem != null){

            super.setProblem(new RunningProblem(aProblem));

            //TODO Test
            System.out.println("StartTimer");

            startTimer(new TimerAction() {

                @Override
                public void onTick(long secondsLeft) {
                    timerInterface.onTick(secondsLeft);
                    getProblem().setTime(secondsLeft);

                    //TODO Test
                    System.out.println(secondsLeft);
                }

                @Override
                public void onFinish() {
                    timerInterface.onFinish();
                }
            });

            //TODO Test
            System.out.println("classResults inicializado");

            classResults.put(aProblem, new HashMap<Solution, Integer>());
            Iterator<Solution> solutionsIterator = aProblem.getSolutions().iterator();
            while (solutionsIterator.hasNext()){
                classResults.get(aProblem).put(solutionsIterator.next(), 0);
            }
            notify(aProblem);
            this.getOperator().execute(new BroadcastOperation(), new FeedBackable() {
                @Override
                public void taskStarted() {
                }

                @Override
                public void taskFinished() {
                }
            });
        }else{
            super.setProblem(aProblem);
        }


    }

    @Override
    public synchronized void newSolution(Solution aSolution) {
        classResults.get(aSolution.getProblem()).put(aSolution, classResults.get(aSolution.getProblem()).get(aSolution)+1);
        notify(aSolution.getProblem());
    }

    private void notify(Problem aProblem) {
        this.setChanged();
        this.notifyObservers(new Par<Problem, Map<Solution, Integer>>(aProblem, classResults.get(aProblem)));
    }

    @Override
    public void disconnect(Object aDisconnectParam) {
        this.clientDisconnected((Integer) aDisconnectParam);
    }

    public void clientDisconnected(int port){
        workers.put(port, workers.get(port)-1);
    }

    @Override
    public void startTimer(TimerAction timerAction) {
        super.startTimer(timerAction);
    }

}
