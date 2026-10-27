package cz.vse.pyraquest.data;

import java.util.Map;

public class Texts {

    private final Map<String, String> texts;

    public Texts(Map<String, String> texts) {
        this.texts = texts;
    }

    public String get(String key) {
        String text = texts.get(key);
        return text != null ? text : "[missing text: " + key + "]";
    }

    // TODO: static factory that loads the texts from JSON in resources
}