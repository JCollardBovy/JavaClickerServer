package ar.unlp.info.laboratorio.javaClickers.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 09/09/13
 * Time: 20:32
 * To change this template use File | Settings | File Templates.
 */
public class ProblemNode  implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 193L;

    private static Integer classId=0;

    private int id;
    private Problem problem;
    private TreeMap<Integer, ProblemNode> nextProblems;

    public ProblemNode(Problem problem, TreeMap<Integer, ProblemNode> nextProblems) {
        this.id = ++classId;
        this.problem = problem;
        this.nextProblems = nextProblems;
    }

    public Problem getProblem() {
        return problem;
    }

    public TreeMap<Integer, ProblemNode> getNextProblems() {
        return nextProblems;
    }

    public ProblemNode next(Integer answerPercentage){
        Iterator<Integer> iterator = nextProblems.keySet().iterator();
        Integer max = null;
        Boolean found = false;
        while ((iterator.hasNext())&&(!found)){
            max = iterator.next();

            //TODO Test
            System.out.println("[ProblemNode] Porcentaje a chequear: "+max+" Porcentaje real: "+answerPercentage);

            found = answerPercentage <= max;
        }
        return nextProblems.get(max);
    }

    @Override
    public String toString() {
        return this.getProblem().getAssigment();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProblemNode)) return false;

        ProblemNode node = (ProblemNode) o;

        if (id!=node.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = problem != null ? problem.hashCode() : 0;
        result = 31 * result + (nextProblems != null ? nextProblems.hashCode() : 0);
        return result;
    }
}
