package ar.unlp.info.laboratorio.javaClickers.model;

import java.io.Serializable;

/**
 * Created by Jony on 03/06/13.
 */
public class Information implements Serializable{

    public static final long serialVersionUID = 188L;
    
    private String subject;
    private String[] topics;

    public Information() {
        this.subject = "";
        topics = new String[0];
    }

    public Information(String subject, String[] topics) {
        this.subject = subject;
        this.topics = topics;
    }

    public String getSubject() {
        return subject;
    }

    public String[] getTopics() {
        return topics;
    }
}
