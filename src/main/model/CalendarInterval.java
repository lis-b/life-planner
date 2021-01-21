package model;

import java.util.Calendar;

// represents an interval of time with start date & time and length in hours and minutes
public class CalendarInterval {
    private Calendar start;
    private Calendar end;
    private int hours;
    private int minutes;

    // EFFECTS: sets up an interval that includes the whole given day
    public CalendarInterval(Calendar date) {
        this.start = initialize(date);
        this.start.set(Calendar.HOUR_OF_DAY, 0);
        this.start.set(Calendar.MINUTE, 0);
        this.hours = 23;
        this.minutes = 59;
        this.updateEnd();
        date.set(Calendar.SECOND, 59);
        date.set(Calendar.MILLISECOND, 59);
    }

    // REQUIRES: hour between 0-23 and minute between 0-59
    // EFFECTS: sets up an interval that starts at the given date and lasts
    //          given hours and minutes
    public CalendarInterval(Calendar date, int hours, int minutes) {
        this.start = initialize(date);
        this.hours = hours;
        this.minutes = minutes;
        this.updateEnd();
    }

    // EFFECTS: clones the given interval into a new one
    public CalendarInterval(CalendarInterval toClone) {
        this.start = initialize(toClone.getStart());
        this.hours = toClone.getHours();
        this.minutes = toClone.getMinutes();
        this.updateEnd();
    }

    public Calendar getStart() {
        return this.start;
    }

    public Calendar getEnd() {
        return this.end;
    }

    public int getHours() {
        return this.hours;
    }

    public int getMinutes() {
        return this.minutes;
    }

    // MODIFIES: this
    // EFFECTS: changes start date and updates the end accordingly
    public void setStart(Calendar date) {
        this.start = initialize(date);
        this.updateEnd();
    }

    // MODIFIES: this
    // EFFECTS: changes end date and updates the hours and minutes accordingly
    public void setEnd(Calendar date) {
        this.end = initialize(date);
        this.updateInterval();
    }

    // MODIFIES: this
    // EFFECTS: changes hour length and updates the end accordingly
    public void setHours(int hours) {
        this.hours = hours;
        this.updateEnd();
    }

    // MODIFIES: this
    // EFFECTS: changes minute length and updates the whole object accordingly
    public void setMinutes(int minutes) {
        this.minutes = minutes;
        this.updateEnd();
        this.updateInterval();
    }

    // EFFECTS: returns true if this interval ends before or exactly as given date starts
    //          otherwise returns false
    public boolean before(Calendar date) {
        return this.end.before(date) || this.end.equals(date);
    }

    // EFFECTS: returns true if given date is within this interval
    //          otherwise returns false
    public boolean during(Calendar date) {
        boolean within = date.after(this.start) && date.before(this.end);
        return within || date.equals(this.start) || date.equals(this.end);
    }

    // EFFECTS: returns true if this interval starts after or exactly as given date starts,
    //          otherwise returns false
    public boolean after(Calendar date) {
        return this.start.after(date) || this.start.equals(date);
    }

    // EFFECTS: primes given date for interval by setting seconds and milliseconds to 0
    private Calendar initialize(Calendar date) {
        Calendar temp = (Calendar) date.clone();
        temp.set(Calendar.SECOND, 0);
        temp.set(Calendar.MILLISECOND, 0);
        return temp;
    }

    // MODIFIES: this
    // EFFECTS: updates the interval's end date and time with the other fields
    private void updateEnd() {
        this.end = (Calendar) this.start.clone();
        this.end.add(Calendar.HOUR_OF_DAY, hours);
        this.end.add(Calendar.MINUTE, minutes);
    }

    // MODIFIES: this
    // EFFECTS: updates the interval's hour and minute lengths with the start and end time
    private void updateInterval() {
        long difference = this.end.getTimeInMillis() - this.start.getTimeInMillis();
        this.hours = (int) difference / 3600000;
        this.minutes = (int) (difference % 3600000) / 60000;
    }
}
