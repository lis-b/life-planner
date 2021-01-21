package persistence;

import org.json.JSONObject;

// interface for classes which can be saved
public interface Saveable {
    // returns this item as JSON object
    JSONObject toJson();
}
