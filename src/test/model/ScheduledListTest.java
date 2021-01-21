package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for ScheduledList class
class ScheduledListTest {
    static Calendar TODAY = Calendar.getInstance();
    static Task TASK_ONE = new Task("One", "", TODAY);
    static Task TASK_TWO = new Task("Two", "hey", TODAY);
    ScheduledList<Task> tasks;

    @BeforeEach
    void runBefore() {
        tasks = new ScheduledList<>("tasks");
    }

    @Test
    void testSettersAndGetters() {
        tasks.addItem(TASK_ONE);
        tasks.addItem(TASK_TWO);
        assertEquals(TASK_ONE, tasks.getItem(0));
        assertEquals(TASK_TWO, tasks.getItem(1));
    }

    @Test
    void testRemoveItem() {
        tasks.addItem(TASK_ONE);
        tasks.addItem(TASK_TWO);
        assertEquals(TASK_ONE, tasks.getItem(0));
        assertEquals(TASK_TWO, tasks.getItem(1));
        assertEquals(2, tasks.length());
        tasks.removeItem(TASK_ONE);
        assertEquals(TASK_TWO, tasks.getItem(0));
        assertEquals(1, tasks.length());
    }

    @Test
    void testListTodayEmpty() {
        assertEquals(0, tasks.length());
        assertEquals(0, tasks.listToday().length());
    }

    @Test
    void testListTodayAllToday() {
        tasks.addItem(TASK_ONE);
        tasks.addItem(TASK_TWO);
        assertEquals(2, tasks.length());
        assertEquals(2, tasks.listToday().length());
    }

    @Test
    void testListTodayOneToday() {
        tasks.addItem(TASK_ONE);
        Calendar inPast = (Calendar) TODAY.clone();
        inPast.set(Calendar.YEAR, 2019);
        Task pastTask = new Task(TASK_ONE);
        pastTask.setDue(inPast);
        tasks.addItem(pastTask);
        assertEquals(2, tasks.length());
        assertEquals(1, tasks.listToday().length());
    }

    @Test
    void testListTodayNoneToday() {
        Calendar inPast = (Calendar) TODAY.clone();
        inPast.set(Calendar.YEAR, 2019);
        Task pastTask = new Task(TASK_ONE);
        pastTask.setDue(inPast);
        tasks.addItem(pastTask);
        assertEquals(1, tasks.length());
        assertEquals(0, tasks.listToday().length());
    }
}