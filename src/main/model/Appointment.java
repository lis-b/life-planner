package model;

import org.json.JSONObject;
import persistence.Saveable;

import java.util.Calendar;

// represents an appointment with a name, description, and timeframe
public class Appointment implements Comparable<Appointment>, Schedulable, Saveable {
    private String name;
    private String description;
    private CalendarInterval timePeriod;

    // EFFECTS: initializes an empty appointment for use in UI
    public Appointment() {
        this.name = "";
        this.description = "";
        this.timePeriod = null;
    }

    public Appointment(Appointment toClone) {
        this.name = toClone.getName();
        this.description = toClone.getDescription();
        this.timePeriod = new CalendarInterval(toClone.getTimePeriod());
    }

    // EFFECTS: initializes an appointment with given name, given description,
    //          starting at the given Calendar object, and lasts for the given,
    //          hours and minutes
    public Appointment(String name, String desc, Calendar date, int hours, int minutes) {
        this.name = name;
        this.description = desc;
        this.timePeriod = new CalendarInterval(date, hours, minutes);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    // EFFECTS: sets time period to given, or null if given null
    public void setTimePeriod(CalendarInterval interval) {
        if (interval != null) {
            this.timePeriod = new CalendarInterval(interval);
        } else {
            this.timePeriod = null;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public CalendarInterval getTimePeriod() {
        return this.timePeriod;
    }

    // EFFECTS: returns starting date/time of appointment
    public Calendar getStartTime() {
        return this.timePeriod.getStart();
    }

    // EFFECTS: returns ending date/time of appointment
    public Calendar getEndTime() {
        return this.timePeriod.getEnd();
    }

    // EFFECTS: returns true if this appointment's starting time is today
    @Override
    public boolean isToday() {
        CalendarInterval today = new CalendarInterval(Calendar.getInstance());
        return today.during(this.timePeriod.getStart());
    }

    // EFFECTS: returns -1 if this appointment starts before given appointment,
    //          returns 0 if this appointment starts at the same time as given
    //          appointment, otherwise returns 1 for this appointment being after
    @Override
    public int compareTo(Appointment other) {
        Calendar thisStart = this.timePeriod.getStart();
        Calendar otherStart = other.getStartTime();
        if (thisStart.equals(otherStart)) {
            return 0;
        } else if (thisStart.before(otherStart)) {
            return -1;
        } else {
            return 1;
        }
    }

    // EFFECTS: returns this appointment as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("name", this.name);
        obj.put("description", this.description);
        obj.put("date", this.timePeriod.getStart().getTimeInMillis());
        obj.put("hours", this.timePeriod.getHours());
        obj.put("minutes", this.timePeriod.getMinutes());
        return obj;
    }
}
