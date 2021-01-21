package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Saveable;

import java.util.ArrayList;
import java.util.Collections;

// represents a list of one type of schedulable items (Appointments or Tasks)
public class ScheduledList<T extends Comparable<T> & Schedulable & Saveable> implements Saveable {
    private ArrayList<T> internalArray;
    private final String type;

    // EFFECTS: initializes new list of schedulable items
    public ScheduledList(String type) {
        this.type = type;
        this.internalArray = new ArrayList<>();
    }

    public String getType() {
        return this.type;
    }

    // REQUIRES: valid index
    // EFFECTS: returns the item at the given index
    public T getItem(int index) {
        return this.internalArray.get(index);
    }

    // MODIFIES: this
    // EFFECTS: adds given item to list & sorts by chronological order
    public void addItem(T newItem) {
        this.internalArray.add(newItem);
        sort();
    }

    // REQUIRES: item already in list
    // MODIFIES: this
    // EFFECTS: removes given item from list
    public void removeItem(T item) {
        this.internalArray.remove(item);
    }

    // EFFECTS: returns number of items in the list
    public int length() {
        return this.internalArray.size();
    }

    // EFFECTS: returns a list of only items that are scheduled for today
    public ScheduledList<T> listToday() {
        ScheduledList<T> newList = new ScheduledList<>(this.type);
        for (T item : this.internalArray) {
            if (item.isToday()) {
                newList.addItem(item);
            }
        }
        return newList;
    }

    // MODIFIES: this
    // EFFECTS: sorts list by chronological order
    private void sort() {
        Collections.sort(this.internalArray);
    }

    // EFFECTS: returns this as a JSON object
    @Override
    public JSONObject toJson() {
        JSONArray arr = new JSONArray();
        for (T t : this.internalArray) {
            arr.put(t.toJson());
        }

        JSONObject obj = new JSONObject();
        obj.put(this.type, arr);
        return obj;
    }
}
