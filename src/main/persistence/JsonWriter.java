package persistence;

import model.*;
import org.json.JSONObject;

import java.io.*;

/**
 * Represents a writer that writes a JSON representation of the main and favourite list to file
*/
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;
    private EventLog log = EventLog.getInstance();

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: uses collection of main recipe list and favourite list, then
    // writes JSON representation of both recipes list to file
    public void write(RecipeLists recipeLists) {
        JSONObject json = recipeLists.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
        log.logEvent(new Event("Saved recipe list to file!"));
    }
}
