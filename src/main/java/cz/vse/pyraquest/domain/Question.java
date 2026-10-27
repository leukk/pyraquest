package cz.vse.pyraquest.domain;

import java.util.*;

public class Question {
    private int id;
    private String text;
    private List<String> options;
    private int correct;

    public int getId() {return id;}

    public String getText() {return text;}

    public List<String> getOptions() {return options;}

    public int getCorrect() {return correct;}

}
