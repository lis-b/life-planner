package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for Tracker class
class TrackerTest {
    static Calendar TODAY = Calendar.getInstance();
    static Calendar IN_PAST = Calendar.getInstance();
    Tracker tracker;

    @BeforeEach
    void runBeforeEach() {
        tracker = new Tracker("Test Tracker");
        IN_PAST.set(Calendar.YEAR, 2019);
    }

    @Test
    void testAddToTrackerEmpty() {
        assertEquals(0, tracker.length());
        tracker.addToTracker(TODAY);
        assertEquals(1, tracker.length());
        assertTrue(tracker.contains(TODAY));
        assertEquals("Test Tracker", tracker.getName());
    }

    @Test
    void testAddToTrackerContainsElement() {
        tracker.addToTracker(IN_PAST);
        assertEquals(1, tracker.length());
        assertTrue(tracker.contains(IN_PAST));
        tracker.addToTracker(TODAY);
        assertEquals(2, tracker.length());
        assertTrue(tracker.contains(IN_PAST));
        assertTrue(tracker.contains(TODAY));
    }

    @Test
    void testMarkDoneEmpty() {
        assertEquals(0, tracker.length());
        tracker.markDone();
        assertEquals(1, tracker.length());
    }

    @Test
    void testMarkDoneContainsElement() {
        tracker.addToTracker(IN_PAST);
        assertEquals(1, tracker.length());
        tracker.markDone();
        assertEquals(2, tracker.length());
    }

    @Test
    void testUnmarkDoneEmpty() {
        assertEquals(0, tracker.length());
        tracker.unmarkDone();
        assertEquals(0, tracker.length());
    }

    @Test
    void testUnmarkDoneOneElement() {
        tracker.addToTracker(TODAY);
        assertEquals(1, tracker.length());
        tracker.unmarkDone();
        assertEquals(0, tracker.length());
    }

    @Test
    void testUnmarkDoneMoreThanOneElement() {
        tracker.addToTracker(IN_PAST);
        tracker.addToTracker(TODAY);
        assertEquals(2, tracker.length());
        tracker.unmarkDone();
        assertEquals(1, tracker.length());
    }

    @Test
    void testIsDoneTodayNeverDone() {
        assertFalse(tracker.isDoneToday());
    }

    @Test
    void testIsDoneTodayDoneInPast() {
        tracker.addToTracker(IN_PAST);
        assertFalse(tracker.isDoneToday());
    }

    @Test
    void testIsDoneTodayOnlyToday() {
        tracker.addToTracker(TODAY);
        assertTrue(tracker.isDoneToday());
    }

    @Test
    void testIsDoneTodayPastAndToday() {
        tracker.addToTracker(IN_PAST);
        tracker.addToTracker(TODAY);
        assertTrue(tracker.isDoneToday());
    }

    @Test
    void testGetLastElementSingle() {
        tracker.addToTracker(IN_PAST);
        assertEquals(IN_PAST, tracker.getLastElement());
    }

    @Test
    void testGetLastElementMultiple() {
        tracker.addToTracker(IN_PAST);
        tracker.addToTracker(TODAY);
        assertEquals(TODAY, tracker.getLastElement());
    }

    @Test
    void testGetLastElementEmpty() {
        assertEquals(0, tracker.length());
        assertNull(tracker.getLastElement());
    }
}