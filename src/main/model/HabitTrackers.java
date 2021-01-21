package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Saveable;

import java.util.ArrayList;

// represents a list of habits to track, just a wrapper class
public class HabitTrackers implements Saveable {
    private ArrayList<Tracker> internalArray;

    // EFFECTS: initializes new blank list of habit trackers
    public HabitTrackers() {
        this.internalArray = new ArrayList<>();
    }

    // EFFECTS: returns tracker at given index
    public Tracker getTracker(int index) {
        return this.internalArray.get(index);
    }

    // MODIFIES: this
    // EFFECTS: adds a tracker to the list
    public void addTracker(Tracker toAdd) {
        this.internalArray.add(toAdd);
    }

    // REQUIRES: valid index
    // MODIFIES this
    // EFFECTS: removes the tracker at given index from the list
    public void removeTracker(int index) {
        this.internalArray.remove(index);
    }

    // EFFECTS: returns number of habit trackers
    public int numberOfTrackers() {
        return this.internalArray.size();
    }

    // EFFECTS: returns true if the tracker is in the list, otherwise returns false
    public boolean containsTracker(Tracker tracker) {
        return this.internalArray.contains(tracker);
    }

    // EFFECTS: returns this as a JSON object
    @Override
    public JSONObject toJson() {
        JSONArray arr = new JSONArray();
        for (Tracker t : this.internalArray) {
            arr.put(t.toJson());
        }

        JSONObject obj = new JSONObject();
        obj.put("habit trackers", arr);
        return obj;
    }
}
