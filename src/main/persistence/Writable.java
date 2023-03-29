package persistence;

import org.json.JSONObject;

/**
 * Describes the behaviour for writing into a JSON file
 */
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
