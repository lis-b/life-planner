package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Saveable;

import java.util.Calendar;
import java.util.ArrayList;

// represents a habit tracker with a name and list of dates completed
public class Tracker implements Saveable {
    private ArrayList<Calendar> internalArray;
    private final String name;

    // EFFECTS: name is set to trackerName & empty list is initialized
    public Tracker(String trackerName) {
        this.internalArray = new ArrayList<>();
        this.name = trackerName;
    }

    public String getName() {
        return this.name;
    }

    // EFFECTS: returns number of dates on tracker list
    public int length() {
        return this.internalArray.size();
    }

    // EFFECTS: returns true if tracker contains given exact date & time,
    //          otherwise false
    public boolean contains(Calendar date) {
        return this.internalArray.contains(date);
    }

    // MODIFIES: this
    // EFFECTS: adds date/Calendar object to tracker
    public void addToTracker(Calendar dateTime) {
        this.internalArray.add(dateTime);
    }

    // MODIFIES: this
    // EFFECTS: add current time to habit tracker
    public void markDone() {
        this.internalArray.add(Calendar.getInstance());
    }

    // MODIFIES: this
    // EFFECTS: removes most recent date from tracker, nothing if tracker is empty
    public void unmarkDone() {
        if (this.internalArray.size() > 0) {
            this.internalArray.remove(this.internalArray.size() - 1);
        }
    }

    // EFFECTS: returns true if habit has been completed today, otherwise returns false
    public boolean isDoneToday() {
        int lastElement = this.internalArray.size() - 1;
        if (lastElement < 0) {
            return false;
        } else {
            CalendarInterval today = new CalendarInterval(Calendar.getInstance());
            return today.during(this.internalArray.get(lastElement));
        }
    }

    // REQUIRES: habit has been completed at least once (internalArray has a size > 0)
    // EFFECTS: returns the most recent date of completion of this habit
    public Calendar getLastElement() {
        if (this.internalArray.size() > 0) {
            int lastElement = this.internalArray.size() - 1;
            return this.internalArray.get(lastElement);
        } else {
            return null;
        }
    }

    // EFFECTS: returns this tracker as a JSON object
    @Override
    public JSONObject toJson() {
        JSONArray arr = new JSONArray();
        for (Calendar c : this.internalArray) {
            arr.put(new JSONObject().put("time", c.getTimeInMillis()));
        }

        JSONObject obj = new JSONObject();
        obj.put("name", this.name);
        obj.put("times complete", arr);
        return obj;
    }
}
