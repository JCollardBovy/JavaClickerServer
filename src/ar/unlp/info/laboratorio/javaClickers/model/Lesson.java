package ar.unlp.info.laboratorio.javaClickers.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 09/09/13
 * Time: 20:27
 * To change this template use File | Settings | File Templates.
 */
public class Lesson implements Serializable{

    /**
     *
     */
    public static final long serialVersionUID = 192L;

    private Information lessonInformation;
    private ArrayList<ProblemNode> indexedProblems;

    public Lesson() {
        this.lessonInformation = new Information();
    }

    public Information getLessonInformation() {
        return lessonInformation;
    }

    public void setLessonInformation(Information lessonInformation) {
        this.lessonInformation = lessonInformation;
    }

    public ArrayList<ProblemNode> getIndexedProblems() {
        return indexedProblems;
    }

    public void setIndexedProblems(ArrayList<ProblemNode> indexedProblems) {
        this.indexedProblems = indexedProblems;
    }
}
