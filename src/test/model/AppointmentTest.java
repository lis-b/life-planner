package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for Appointment class
class AppointmentTest {
    static Calendar TODAY = Calendar.getInstance();
    static Calendar IN_PAST = Calendar.getInstance();
    static int HOUR = 2;
    static int MINUTE = 30;
    Appointment appointment;
    Appointment past;

    @BeforeEach
    void runBefore() {
        appointment = new Appointment("Test Appointment", "", TODAY, HOUR, MINUTE);
        IN_PAST.set(Calendar.YEAR, 2019);
        TODAY.set(Calendar.SECOND, 0);
        TODAY.set(Calendar.MILLISECOND, 0);
        past = new Appointment("Test Appointment", "", IN_PAST, HOUR, MINUTE);
    }

    @Test
    void testSettersGetters() {
        appointment.setName("Hi");
        appointment.setDescription("Hello");
        assertEquals("Hi", appointment.getName());
        assertEquals("Hello", appointment.getDescription());
    }

    @Test
    void testEmptyConstructor() {
        Appointment empty = new Appointment();
        assertEquals("", empty.getName());
        assertEquals("", empty.getDescription());
        assertNull(empty.getTimePeriod());
    }

    @Test
    void testGetStartTime() {
        assertEquals(TODAY, appointment.getStartTime());
    }

    @Test
    void testGetEndTime() {
        Calendar afterAppointment = (Calendar) TODAY.clone();
        afterAppointment.add(Calendar.HOUR_OF_DAY, HOUR);
        afterAppointment.add(Calendar.MINUTE, MINUTE);

        assertEquals(afterAppointment, appointment.getEndTime());
    }

    @Test
    void setTimePeriodNull() {
        appointment.setTimePeriod(null);
        assertNull(appointment.getTimePeriod());
    }

    @Test
    void setTimePeriodNotNull() {
        CalendarInterval newTime = new CalendarInterval(IN_PAST, 2, 30);
        appointment.setTimePeriod(newTime);
        assertEquals(newTime.getStart(), appointment.getTimePeriod().getStart());
        assertEquals(newTime.getEnd(), appointment.getTimePeriod().getEnd());
    }

    @Test
    void testIsTodayTrue() {
        assertTrue(appointment.isToday());
    }

    @Test
    void testIsTodayFalse() {
        assertFalse(past.isToday());
    }

    @Test
    void testCompareToBefore() {
        assertEquals(-1, past.compareTo(appointment));
    }

    @Test
    void testCompareToSameTime() {
        Appointment pastClone = new Appointment(past);
        Appointment todayClone = new Appointment(appointment);
        assertEquals(0, past.compareTo(pastClone));
        assertEquals(0, appointment.compareTo(todayClone));
    }

    @Test
    void testCompareToAfter() {
        assertEquals(1, appointment.compareTo(past));
    }
}