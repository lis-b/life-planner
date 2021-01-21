package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.stream.Stream;

// Represents a loader to load the schedule from a saved file
// created by referencing CPSC210's JsonSerializationDemo
public class JsonLoader {
    private String fileName;

    // EFFECTS: constructs loader in reference to given filename
    public JsonLoader(String fileName) {
        this.fileName = fileName;
    }

    // EFFECTS: loads a saved schedule from this file;
    //          throws IOException if an error occurs loading
    private String readFile(String file) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(file), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    // EFFECTS: loads saved appointments from this file;
    //          throws IOException if an error occurs loading
    public ScheduledList<Appointment> loadAppointments() throws IOException {
        String data = readFile(fileName);
        JSONObject obj = new JSONObject(data);
        return parseAppointments(obj.getJSONArray("saved schedule").getJSONObject(0));
    }

    // EFFECTS: parses appointments from JSON object and returns it
    private ScheduledList<Appointment> parseAppointments(JSONObject obj) {
        JSONArray array = obj.getJSONArray("appointments");
        ScheduledList<Appointment> apt = new ScheduledList<>("appointments");
        for (Object json : array) {
            JSONObject nextItem = (JSONObject) json;
            addAppointment(apt, nextItem);
        }
        return apt;
    }

    // EFFECTS: parses an appointment from JSON object and adds it
    //          to given list
    private void addAppointment(ScheduledList<Appointment> apt, JSONObject item) {
        String name = item.getString("name");
        String desc = item.getString("description");
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(item.getLong("date"));
        int hours = item.getInt("hours");
        int minutes = item.getInt("minutes");

        Appointment appointment = new Appointment(name, desc, date, hours, minutes);
        apt.addItem(appointment);
    }

    // EFFECTS: loads saved tasks from this file;
    //          throws IOException if an error occurs loading
    public ScheduledList<Task> loadTasks() throws IOException {
        String data = readFile(fileName);
        JSONObject obj = new JSONObject(data);
        return parseTasks(obj.getJSONArray("saved schedule").getJSONObject(1));
    }

    // EFFECTS: parses tasks from JSON object and returns it
    private ScheduledList<Task> parseTasks(JSONObject obj) {
        JSONArray array = obj.getJSONArray("tasks");
        ScheduledList<Task> tasks = new ScheduledList<>("tasks");
        for (Object json : array) {
            JSONObject nextItem = (JSONObject) json;
            addTask(tasks, nextItem);
        }
        return tasks;
    }

    // EFFECTS: parses a task from JSON object and adds it
    //          to given list
    private void addTask(ScheduledList<Task> tasks, JSONObject item) {
        String name = item.getString("name");
        String desc = item.getString("description");
        Calendar due = Calendar.getInstance();
        due.setTimeInMillis(item.getLong("due"));
        boolean complete = item.getBoolean("completion");

        Task task = new Task(name, desc, due);

        if (complete) {
            task.toggleCompletion();
        }

        tasks.addItem(task);
    }

    // EFFECTS: loads saved habit trackers from this file;
    //          throws IOException if an error occurs loading
    public HabitTrackers loadHabits() throws IOException {
        String data = readFile(fileName);
        JSONObject obj = new JSONObject(data);
        return parseTrackers(obj.getJSONArray("saved schedule").getJSONObject(2));
    }

    // EFFECTS: parses habit trackers from JSON object and returns it
    private HabitTrackers parseTrackers(JSONObject obj) {
        JSONArray array = obj.getJSONArray("habit trackers");
        HabitTrackers ht = new HabitTrackers();
        for (Object json : array) {
            JSONObject nextItem = (JSONObject) json;
            addTracker(ht, nextItem);
        }
        return ht;
    }

    // EFFECTS: parses an individual tracker and adds it to given
    //          habit tracker list
    private void addTracker(HabitTrackers ht, JSONObject item) {
        String name = item.getString("name");
        Tracker tracker = new Tracker(name);

        addTrackerDates(tracker, item);

        ht.addTracker(tracker);
    }

    // EFFECTS: parses the completed dates of a tracker and adds it to
    //          given tracker
    private void addTrackerDates(Tracker tracker, JSONObject item) {
        JSONArray arr = item.getJSONArray("times complete");
        for (Object json : arr) {
            JSONObject nextItem = (JSONObject) json;
            long date = nextItem.getLong("time");
            addDate(tracker, date);
        }
    }

    // EFFECTS: parses a single completed dates of a tracker and adds it to
    //          given tracker
    private void addDate(Tracker tracker, Long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        tracker.addToTracker(cal);
    }
}
