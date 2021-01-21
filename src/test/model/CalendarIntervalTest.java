package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for CalendarInterval class
class CalendarIntervalTest {
    static Calendar TODAY = Calendar.getInstance();
    static CalendarInterval TODAY_INTERVAL = new CalendarInterval(TODAY);
    static int HOUR = 2;
    static int MINUTE = 2;
    CalendarInterval interval;

    @BeforeEach
    void runBefore() {
        interval = new CalendarInterval(TODAY, HOUR, MINUTE);
    }

    @Test
    void testConstructorClone() {
        CalendarInterval newInterval = new CalendarInterval(interval);
        assertEquals(newInterval.getStart(), interval.getStart());
        assertEquals(newInterval.getEnd(), interval.getEnd());
        assertEquals(newInterval.getHours(), interval.getHours());
        assertEquals(newInterval.getMinutes(), interval.getMinutes());
    }

    @Test
    void testSetStart() {
        Calendar timeChange = (Calendar) TODAY.clone();
        int hourChange = 1;
        int minuteChange = 15;
        timeChange.add(Calendar.HOUR_OF_DAY, hourChange);
        timeChange.add(Calendar.MINUTE, minuteChange);
        timeChange.set(Calendar.SECOND, 0);
        timeChange.set(Calendar.MILLISECOND, 0);
        interval.setStart(timeChange);
        assertEquals(timeChange, interval.getStart());
        Calendar timeChangeEnd = (Calendar) timeChange.clone();
        timeChangeEnd.add(Calendar.HOUR_OF_DAY, HOUR);
        timeChangeEnd.add(Calendar.MINUTE, MINUTE);
        assertEquals(timeChangeEnd, interval.getEnd());
    }

    @Test
    void testSetEnd() {
        Calendar initialStart = (Calendar) interval.getStart().clone();
        Calendar timeChange = (Calendar) TODAY.clone();
        int hourChange = 4;
        int minuteChange = 15;
        timeChange.add(Calendar.HOUR_OF_DAY, hourChange);
        timeChange.add(Calendar.MINUTE, minuteChange);
        timeChange.set(Calendar.SECOND, 0);
        timeChange.set(Calendar.MILLISECOND, 0);
        interval.setEnd(timeChange);
        assertEquals(initialStart, interval.getStart());
        assertEquals(timeChange, interval.getEnd());
        assertEquals(hourChange, interval.getHours());
        assertEquals(minuteChange, interval.getMinutes());
    }

    @Test
    void testSetHoursAndMinutes() {
        Calendar initialStart = (Calendar) interval.getStart().clone();
        int hourChange = 5;
        int minuteChange = 15;
        Calendar newEnd = (Calendar) interval.getStart().clone();
        newEnd.add(Calendar.HOUR_OF_DAY, hourChange);
        newEnd.add(Calendar.MINUTE, minuteChange);
        interval.setHours(hourChange);
        interval.setMinutes(minuteChange);
        assertEquals(initialStart, interval.getStart());
        assertEquals(newEnd, interval.getEnd());
    }

    @Test
    void testIntervalBeforeTrue() {
        Calendar otherDay = (Calendar) TODAY.clone();
        otherDay.set(Calendar.YEAR, 2050);
        assertTrue(TODAY_INTERVAL.before(otherDay));
    }

    @Test
    void testIntervalBeforeFalse() {
        Calendar otherDay = (Calendar) TODAY.clone();
        otherDay.set(Calendar.YEAR, 2019);
        assertFalse(TODAY_INTERVAL.before(otherDay));
    }

    @Test
    void testIntervalBeforeBound() {
        Calendar otherDay = (Calendar) TODAY.clone();
        otherDay.set(Calendar.HOUR_OF_DAY, TODAY_INTERVAL.getEnd().get(Calendar.HOUR_OF_DAY));
        otherDay.set(Calendar.MINUTE, TODAY_INTERVAL.getEnd().get(Calendar.MINUTE));
        otherDay.set(Calendar.SECOND, TODAY_INTERVAL.getEnd().get(Calendar.SECOND));
        otherDay.set(Calendar.MILLISECOND, TODAY_INTERVAL.getEnd().get(Calendar.MILLISECOND));
        assertTrue(TODAY_INTERVAL.before(otherDay));
    }

    @Test
    void testIntervalDuringTrue() {
        assertTrue(TODAY_INTERVAL.during(TODAY));
    }

    @Test
    void testIntervalDuringFalse() {
        Calendar otherDay = (Calendar) TODAY.clone();
        otherDay.set(Calendar.YEAR, 2019);
        assertFalse(TODAY_INTERVAL.during(otherDay));
    }

    @Test
    void testIntervalDuringStartBound() {
        Calendar otherDay = (Calendar) TODAY.clone();
        otherDay.set(Calendar.HOUR_OF_DAY, TODAY_INTERVAL.getStart().get(Calendar.HOUR_OF_DAY));
        otherDay.set(Calendar.MINUTE, TODAY_INTERVAL.getStart().get(Calendar.MINUTE));
        otherDay.set(Calendar.SECOND, TODAY_INTERVAL.getStart().get(Calendar.SECOND));
        otherDay.set(Calendar.MILLISECOND, TODAY_INTERVAL.getStart().get(Calendar.MILLISECOND));
        assertTrue(TODAY_INTERVAL.during(otherDay));
    }

    @Test
    void testIntervalDuringEndBound() {
        Calendar otherDay = (Calendar) TODAY.clone();
        otherDay.set(Calendar.HOUR_OF_DAY, TODAY_INTERVAL.getEnd().get(Calendar.HOUR_OF_DAY));
        otherDay.set(Calendar.MINUTE, TODAY_INTERVAL.getEnd().get(Calendar.MINUTE));
        otherDay.set(Calendar.SECOND, TODAY_INTERVAL.getEnd().get(Calendar.SECOND));
        otherDay.set(Calendar.MILLISECOND, TODAY_INTERVAL.getEnd().get(Calendar.MILLISECOND));
        assertTrue(TODAY_INTERVAL.during(otherDay));
    }

    @Test
    void testIntervalAfterTrue() {
        Calendar otherDay = (Calendar) TODAY.clone();
        otherDay.set(Calendar.YEAR, 2019);
        assertTrue(TODAY_INTERVAL.after(otherDay));
    }

    @Test
    void testIntervalAfterFalse() {
        Calendar otherDay = (Calendar) TODAY.clone();
        otherDay.set(Calendar.YEAR, 2050);
        assertFalse(TODAY_INTERVAL.after(otherDay));
    }

    @Test
    void testIntervalAfterBound() {
        Calendar otherDay = (Calendar) TODAY.clone();
        otherDay.set(Calendar.HOUR_OF_DAY, TODAY_INTERVAL.getStart().get(Calendar.HOUR_OF_DAY));
        otherDay.set(Calendar.MINUTE, TODAY_INTERVAL.getStart().get(Calendar.MINUTE));
        otherDay.set(Calendar.SECOND, TODAY_INTERVAL.getStart().get(Calendar.SECOND));
        otherDay.set(Calendar.MILLISECOND, TODAY_INTERVAL.getStart().get(Calendar.MILLISECOND));
        assertTrue(TODAY_INTERVAL.after(otherDay));
    }
}