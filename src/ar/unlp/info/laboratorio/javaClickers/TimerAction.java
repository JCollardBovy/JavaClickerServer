package ar.unlp.info.laboratorio.javaClickers;

/**
 * Created by Jony on 07/07/13.
 */
public interface TimerAction {

    public void onTick(long secondsLeft);
    public void onFinish();
}
