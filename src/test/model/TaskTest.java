package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for Task class
class TaskTest {
    static Calendar TODAY = Calendar.getInstance();
    Task task;

    @BeforeEach
    void runBefore() {
        task = new Task("Test", "", TODAY);
    }

    @Test
    void testNullConstructor() {
        Task nullTask = new Task();
        assertEquals("", nullTask.getName());
        assertEquals("", nullTask.getDescription());
        assertNull(nullTask.getDue());
        assertFalse(nullTask.isComplete());
    }

    @Test
    void testSettersGetters() {
        task.setName("Changed Name");
        task.setDescription("Changed Description");
        assertEquals("Changed Name", task.getName());
        assertEquals("Changed Description", task.getDescription());

        task.setDue(null);
        assertNull(task.getDue());
        task.setDue(TODAY);
        assertEquals(TODAY, task.getDue());
    }

    @Test
    void testToggleCompletion() {
        assertFalse(task.isComplete());
        task.toggleCompletion();
        assertTrue(task.isComplete());
        task.toggleCompletion();
        assertFalse(task.isComplete());
    }

    @Test
    void testCompareToBefore() {
        Calendar inPast = (Calendar) TODAY.clone();
        inPast.set(Calendar.YEAR, 2019);
        Task pastTask = new Task(task);
        pastTask.setDue(inPast);
        assertEquals(-1, pastTask.compareTo(task));
    }

    @Test
    void testCompareToSame() {
        Task otherTask = new Task(task);
        assertEquals(0, otherTask.compareTo(task));
    }

    @Test
    void testCompareToAfter() {
        Calendar inPast = (Calendar) TODAY.clone();
        inPast.set(Calendar.YEAR, 2019);
        Task pastTask = new Task(task);
        pastTask.setDue(inPast);
        assertEquals(1, task.compareTo(pastTask));
    }

    @Test
    void testIsTodayTrue() {
        assertTrue(task.isToday());
    }

    @Test
    void testIsTodayFalse() {
        Calendar inPast = (Calendar) TODAY.clone();
        inPast.set(Calendar.YEAR, 2019);
        task.setDue(inPast);
        assertFalse(task.isToday());
    }
}