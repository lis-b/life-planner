package model;

// interface for classes which are able to be scheduled
public interface Schedulable {
    // EFFECTS: returns true if item is dated today
    boolean isToday();
}
