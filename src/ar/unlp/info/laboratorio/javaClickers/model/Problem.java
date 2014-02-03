package ar.unlp.info.laboratorio.javaClickers.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jony on 05/07/13.
 */
public class Problem implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 189L;

    private static Integer classId=0;

    /**
     * El enunciado del problema a resolver
     */
    private String assigment;

    /**
     * Tiempo definido para resolver el problema
     */
    private long time;

    /**
     * Posibles solutions del problema
     */
    private Collection<Solution> solutions;

    /**
     *
     */
    private Solution correctSolution;

    private int id;

    /**
     * Constructor que genera un nuevo problema a partir de una assigment y un conjunto de posibles solutions
     * @param assigment		Enunciado del problema
     * @param solutions	Posibles solutions del problema
     */
    public Problem(String assigment, Collection<Solution> solutions) {
        super();
        this.assigment = assigment;
        this.solutions = solutions;
        this.id = ++classId;
    }

    /**
     * Constructor que genera un nuevo problema a partir de una assigment, un time y un conjunto de posibles solutions
     * @param assigment		Enunciado del problema
     * @param time		Tiempo para resolver el problema
     * @param solutions	Posibles solutions del problema
     */
    public Problem(String assigment, long time, Collection<Solution> solutions, Solution correctSolution) {
        this(assigment, solutions);
        this.time = time;
        this.correctSolution = correctSolution;
    }

    public Problem() {
    }

    /**
     * Retorna el enunciado a resolver
     * @return	Enunciado del problema
     */
    public String getAssigment() {
        return assigment;
    }

    /**
     * Retorna el conjunto de solutions al problema
     * @return	Coleccion con solutions al problema
     */
    public Collection<Solution> getSolutions() {
        return solutions;
    }

    /**
     * Retorna el time definido para resolver el problema
     * @return	Numero entero con el time en segundos
     */
    public long getTime() {
        return time;
    }

    public Solution getCorrectSolution() {
        return correctSolution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Problem)) return false;

        Problem problem = (Problem) o;

        if (id!=problem.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = assigment != null ? assigment.hashCode() : 0;
        result = 31 * result + (solutions != null ? solutions.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }

    public void setAssigment(String assigment) {
        this.assigment = assigment;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setCorrectSolution(Solution correctSolution) {
        this.correctSolution = correctSolution;
    }

    public void setSolutions(Collection<Solution> solutions) {
        this.solutions = solutions;
    }

}
