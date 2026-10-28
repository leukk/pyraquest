package cz.vse.pyraquest.domain;

import java.util.*;

public class Question {
    private final int id;
    private final String text;
    private final List<String> options;
    private final int correct;

    public Question(int id, String text, List<String> options, int correct) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.correct = correct;
    }

    public int getId() {return id;}

    public String getText() {return text;}

    public List<String> getOptions() {return options;}

    public int getCorrect() {return correct;}

}
