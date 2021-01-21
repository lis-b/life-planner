package persistence;

import model.Appointment;
import model.HabitTrackers;
import model.ScheduledList;
import model.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a saver to save the schedule to a saved file
// created by referencing CPSC210's JsonSerializationDemo
public class JsonSaver {
    private static final int INDENT = 4;
    private PrintWriter writer;
    private String fileName;

    // EFFECTS: constructs saver to save at given filename
    public JsonSaver(String fileName) {
        this.fileName = fileName;
    }

    // MODIFIES: this
    // EFFECTS: opens saver, throws FileNotFoundException if unable
    //          to open file
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(fileName));
    }

    // MODIFIES: this
    // EFFECTS: saves a JSON representation of the schedule to file
    public void save(ScheduledList<Appointment> apt, ScheduledList<Task> tasks, HabitTrackers ht) {
        JSONArray arr = new JSONArray();
        arr.put(apt.toJson());
        arr.put(tasks.toJson());
        arr.put(ht.toJson());

        JSONObject obj = new JSONObject();
        obj.put("saved schedule", arr);
        writer.print(obj.toString(INDENT));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }
}
