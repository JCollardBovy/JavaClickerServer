package ar.unlp.info.laboratorio.javaClickers.network;

import ar.unlp.info.laboratorio.javaClickers.TimerAction;
import ar.unlp.info.laboratorio.javaClickers.network.operations.Operation;
import ar.unlp.info.laboratorio.javaClickers.FeedBackable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 05/07/13
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
public class JavaOperator extends Operator {

    Timer timer;

    public JavaOperator() {
        timer = new Timer();
    }

    public void executeTask(Operation operation, FeedBackable aFeedBackInterface) {
        new Thread(new ExecuteTask(operation, aFeedBackInterface)).start();
    }

    @Override
    public void startTimer(final long secondsLeft, final TimerAction timerAction) {
        timer.scheduleAtFixedRate(new TimerTask() {

            long time=secondsLeft;

            @Override
            public void run() {
                timerAction.onTick(--time);
                if (time <= 0){
                    Manager.getInstance().setProblem(null);
                    cancel();
                    timerAction.onFinish();
                }
            }
        }, 0, 1000);
    }


    private static class ExecuteTask implements Runnable{

        Operation operation;
        FeedBackable feedBackInterface;

        private ExecuteTask(Operation operation, FeedBackable feedBackInterface) {
            this.operation = operation;
            this.feedBackInterface = feedBackInterface;
        }

        @Override
        public void run(){
            operation.executeOnServer(null);
            feedBackInterface.taskFinished();
        }
    }
}
