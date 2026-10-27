package cz.vse.pyraquest.data;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Texts {

    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {
    }.getType();

    private final Map<String, String> texts;

    public Texts(Map<String, String> texts) {
        this.texts = texts;
    }

    public String get(String key) {
        String text = texts.get(key);
        return text != null ? text : "[missing text: " + key + "]";
    }

    public static Texts fromJson(String json) {
        return new Texts(parse(new StringReader(json), "JSON string"));
    }

    public static Texts fromResources(String... paths) {
        Map<String, String> all = new HashMap<>();
        for (String path : paths) {
            try (InputStream in = Texts.class.getResourceAsStream(path)) {
                if (in == null) {
                    throw new IllegalStateException("Text file not found: " + path);
                }
                Map<String, String> part = parse(new InputStreamReader(in, StandardCharsets.UTF_8), path);
                for (Map.Entry<String, String> entry : part.entrySet()) {
                    if (all.putIfAbsent(entry.getKey(), entry.getValue()) != null) {
                        throw new IllegalStateException("Duplicate text key: " + entry.getKey());
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException("Cannot read " + path, e);
            }
        }
        return new Texts(all);
    }

    private static Map<String, String> parse(Reader reader, String source) {
        try {
            Map<String, String> map = GSON.fromJson(reader, MAP_TYPE);
            return map != null ? map : new HashMap<>();
        } catch (JsonParseException e) {
            throw new IllegalStateException("Invalid JSON in " + source, e);
        }
    }
}