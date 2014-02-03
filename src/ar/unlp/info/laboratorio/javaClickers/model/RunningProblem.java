package ar.unlp.info.laboratorio.javaClickers.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 30/09/13
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public class RunningProblem extends Problem implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 191L;

    private long remainingTime;
    private Problem aProblem;

    public RunningProblem(Problem aProblem){
        this.aProblem = aProblem;
        this.remainingTime = aProblem.getTime();
    }

    @Override
    public long getTime() {
        return this.remainingTime;
    }

    @Override
    public void setTime(long time) {
        remainingTime = time;
    }

    @Override
    public String getAssigment() {
        return aProblem.getAssigment();
    }

    @Override
    public Collection<Solution> getSolutions() {
        return aProblem.getSolutions();
    }

    @Override
    public Solution getCorrectSolution() {
        return aProblem.getCorrectSolution();
    }

    @Override
    public boolean equals(Object o) {
        return aProblem.equals(o);
    }

    @Override
    public int hashCode() {
        return aProblem.hashCode();
    }

    @Override
    public void setAssigment(String assigment) {
        aProblem.setAssigment(assigment);
    }

    @Override
    public void setCorrectSolution(Solution correctSolution) {
        aProblem.setCorrectSolution(correctSolution);
    }

    @Override
    public void setSolutions(Collection<Solution> solutions) {
        aProblem.setSolutions(solutions);
    }
}
