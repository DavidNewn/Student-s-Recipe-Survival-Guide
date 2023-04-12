package persistence;

import org.json.JSONObject;

/**
 * Represents the behaviour for writing into a JSON file
 */
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
