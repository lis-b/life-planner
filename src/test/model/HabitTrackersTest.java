package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for HabitTrackers class
class HabitTrackersTest {
    static Tracker TEST_TRACKER;
    static Tracker ANOTHER_TRACKER;
    HabitTrackers habits;

    @BeforeEach
    void runBeforeEach() {
        habits = new HabitTrackers();
        TEST_TRACKER = new Tracker("Test");
        ANOTHER_TRACKER = new Tracker("Other Test");
    }

    @Test
    void testAddTrackerToEmpty() {
        habits.addTracker(TEST_TRACKER);
        assertEquals(TEST_TRACKER, habits.getTracker(0));
        assertTrue(habits.containsTracker(TEST_TRACKER));
    }

    @Test
    void testAddTrackerToNonEmpty() {
        habits.addTracker(TEST_TRACKER);
        habits.addTracker(ANOTHER_TRACKER);
        assertEquals(ANOTHER_TRACKER, habits.getTracker(1));
        assertTrue(habits.containsTracker(ANOTHER_TRACKER));
    }

    @Test
    void testRemoveTrackerOnlyOne() {
        habits.addTracker(TEST_TRACKER);
        assertTrue(habits.containsTracker(TEST_TRACKER));
        habits.removeTracker(0);
        assertFalse(habits.containsTracker(TEST_TRACKER));
        assertEquals(0, habits.numberOfTrackers());
    }

    @Test
    void testRemoveTrackerAnotherInList() {
        habits.addTracker(TEST_TRACKER);
        habits.addTracker(ANOTHER_TRACKER);
        assertTrue(habits.containsTracker(TEST_TRACKER));
        assertTrue(habits.containsTracker(ANOTHER_TRACKER));
        habits.removeTracker(0);
        assertFalse(habits.containsTracker(TEST_TRACKER));
        assertEquals(1, habits.numberOfTrackers());
        assertEquals(ANOTHER_TRACKER, habits.getTracker(0));
        assertTrue(habits.containsTracker(ANOTHER_TRACKER));
    }
}