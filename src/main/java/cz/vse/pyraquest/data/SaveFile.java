package cz.vse.pyraquest.data;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

public class SaveFile {

    private static final Gson GSON = new Gson();
    private static final String VALID_NAME = "[a-zA-Z0-9_-]+";

    public record Data(long seed, List<String> moves) {
    }

    private final Path directory;

    public SaveFile(Path directory) {
        this.directory = directory;
    }

    public void write(String name, Data data) throws IOException {
        try{
            Path file = path(name);
            Files.createDirectories(directory);
            Files.writeString(file, GSON.toJson(data));
        } catch (IOException e){
            throw new IOException("Nepodařilo se uložit hru: " + name, e);
        }
    }

    public Data read(String name) throws IOException {
        try {
            String json = Files.readString(path(name));
            Data data = GSON.fromJson(json, Data.class);

            if (data == null || data.moves() == null) {
                throw new JsonParseException("Missing moves in save " + name);
            }
            return data;
        } catch (NoSuchFileException e) {
            throw new IOException("Uložená hra neexistuje: " + name, e);
        } catch (IOException e) {
            throw new IOException("Nepodařilo se načíst hru: " + name, e);
        } catch (JsonParseException e) {
            throw new IOException("Poškozený soubor uložené hry: " + name, e);
        }
    }

    private Path path(String name) throws NoSuchFileException {
        if (!name.matches(VALID_NAME)) {
            throw new NoSuchFileException(name);
        }
        return directory.resolve(name + ".json");
    }
}
