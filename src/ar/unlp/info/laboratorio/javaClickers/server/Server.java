package ar.unlp.info.laboratorio.javaClickers.server;

import ar.unlp.info.laboratorio.javaClickers.FeedBackable;
import ar.unlp.info.laboratorio.javaClickers.TimerAction;
import ar.unlp.info.laboratorio.javaClickers.model.Information;
import ar.unlp.info.laboratorio.javaClickers.model.Problem;
import ar.unlp.info.laboratorio.javaClickers.model.ProblemNode;
import ar.unlp.info.laboratorio.javaClickers.model.Solution;
import ar.unlp.info.laboratorio.javaClickers.auxiliary.Par;
import ar.unlp.info.laboratorio.javaClickers.network.ServerManager;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 05/07/13
 * Time: 18:44
 * To change this template use File | Settings | File Templates.
 */
public class Server extends Observable implements Observer{

    ProblemNode currentProblem;
    private Map<Problem, Map<Solution, Integer>> statistics = new HashMap<Problem, Map<Solution, Integer>>();

    public Server(){
        ServerManager.getInstance().addObserver(this);
        ServerManager.getInstance().listenForClients(new ClientFeedBack());
    }

    @Override
    public void update(Observable o, Object arg) {
        Par<Problem, Map<Solution, Integer>> statisticData = (Par<Problem, Map<Solution, Integer>>) arg;
        statistics.put(statisticData.getKey(), statisticData.getValue());

        setChanged();
        notifyObservers(arg);


        //TODO Test
        System.out.println(statisticData.getKey().getAssigment());
        Iterator<Solution> iterator = statisticData.getValue().keySet().iterator();
        while (iterator.hasNext()){
            Solution sol = iterator.next();
            System.out.print(sol.getDescription());
            System.out.println(statisticData.getValue().get(sol));
        }
    }

    public void loadInformation(Information lessonInformation) {
        ServerManager.getInstance().setInformation(lessonInformation);
    }

    private class ClientFeedBack implements FeedBackable {
        @Override
        public void taskStarted() {
        }

        @Override
        public void taskFinished() {
        }
    }

    public void run(ProblemNode aProblemNode, Information anInformation, TimerAction timerInferface){
        ServerManager.getInstance().setInformation(anInformation);
        currentProblem = aProblemNode;
        ServerManager.getInstance().setProblem(aProblemNode.getProblem(), timerInferface);
    }

    public Problem next(TimerAction timerInterface){
        Integer answerCount = 0;
        for (Integer a : statistics.get(currentProblem.getProblem()).values()){
            answerCount += a;
        }
        int answerPercentage = 0;
        if (answerCount != 0){
            answerPercentage = (statistics.get(currentProblem.getProblem()).get(currentProblem.getProblem().getCorrectSolution())/answerCount)*100;
        }

        //TODO Test
        System.out.println("Cantidad Correctos: "+statistics.get(currentProblem.getProblem()).get(currentProblem.getProblem().getCorrectSolution()));
        System.out.println("Cantidad Total: "+answerCount);
        System.out.println("Porcentaje: "+answerPercentage);

        currentProblem = currentProblem.next(answerPercentage);
        ServerManager.getInstance().setProblem(currentProblem.getProblem(), timerInterface);
        return currentProblem.getProblem();

    }

}
