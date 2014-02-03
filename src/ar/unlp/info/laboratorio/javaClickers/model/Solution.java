package ar.unlp.info.laboratorio.javaClickers.model;

import java.io.Serializable;

/**
 * Created by Jony on 05/07/13.
 */
public class Solution implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 190L;

    private static Integer classId=0;

    private int id;
    private String description;
    private Problem problem;

    public Solution(String description, Problem problema) {
        super();
        this.description = description;
        this.problem = problema;
        this.id = ++classId;
    }

    public Solution(int id, String description) {
        super();
        this.description = description;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.description;
    }

    public Problem getProblem() {
        return problem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Solution)) return false;

        Solution solution = (Solution) o;

        if (id != solution.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
