package ar.unlp.info.laboratorio.javaClickers.network;

import ar.unlp.info.laboratorio.javaClickers.TimerAction;
import ar.unlp.info.laboratorio.javaClickers.network.operations.Operation;
import ar.unlp.info.laboratorio.javaClickers.FeedBackable;

/**
 * Created by Jony on 23/05/13.
 */
public abstract class Operator {
    public static int broadcastPort = 45678;

    public void execute(Operation operation, FeedBackable aFeedBackInterface) {
        aFeedBackInterface.taskStarted();
        executeTask(operation, aFeedBackInterface);
    }

    protected abstract void executeTask(Operation operation, FeedBackable aFeedBackInterface);

    public abstract void startTimer(long secondsLeft, TimerAction timerAction);
}
