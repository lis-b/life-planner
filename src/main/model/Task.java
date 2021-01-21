package model;

import org.json.JSONObject;
import persistence.Saveable;

import java.util.Calendar;

// represents a task with a name, description, due date, and completion
public class Task implements Comparable<Task>, Schedulable, Saveable {
    private String name;
    private String description;
    private Calendar due;
    private boolean complete;

    // EFFECTS: initializes a blank incomplete task for use in UI
    public Task() {
        this.name = "";
        this.description = "";
        this.due = null;
        this.complete = false;
    }

    // EFFECTS: initializes a cloned task
    public Task(Task other) {
        this.name = other.getName();
        this.description = other.getDescription();
        this.due = (Calendar) other.getDue().clone();
        this.complete = other.isComplete();
    }

    // EFFECTS: initializes an incomplete task with given name, description, and due date
    public Task(String name, String desc, Calendar due) {
        this.name = name;
        this.description = desc;
        this.due = (Calendar) due.clone();
        this.complete = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Calendar getDue() {
        return this.due;
    }

    // MODIFIES: this
    // EFFECTS: set due date to given due date, sets to null if given null
    public void setDue(Calendar due) {
        if (due != null) {
            this.due = (Calendar) due.clone();
        } else {
            this.due = null;
        }
    }

    // MODIFIES: this
    // EFFECTS: changes complete tasks to incomplete tasks and vice versa
    public void toggleCompletion() {
        this.complete = !this.complete;
    }

    // EFFECTS: returns true if task is complete, otherwise returns false
    public boolean isComplete() {
        return this.complete;
    }

    // EFFECTS: returns -1 if this task starts before given task,
    //          returns 0 if this task starts at the same time as given
    //          task, otherwise returns 1 for this task being after
    @Override
    public int compareTo(Task other) {
        if (this.due.equals(other.getDue())) {
            return 0;
        } else if (this.due.before(other.getDue())) {
            return -1;
        } else {
            return 1;
        }
    }

    // EFFECTS: returns true if task is due today
    @Override
    public boolean isToday() {
        CalendarInterval today = new CalendarInterval(Calendar.getInstance());
        return today.during(this.due);
    }

    // EFFECTS: returns this task as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("name", this.name);
        obj.put("description", this.description);
        obj.put("due", this.due.getTimeInMillis());
        obj.put("completion", this.complete);
        return obj;
    }
}
