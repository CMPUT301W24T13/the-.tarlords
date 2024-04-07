package com.example.the_tarlords;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.the_tarlords.data.Alert.Alert;

import java.time.LocalDateTime;

/**
 * Testing the Alert class
 */
public class AlertTest {

    private Alert alert;

    @Before
    public void setUp() {
        alert = new Alert("Test Title", "Test Message", null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(alert);
        assertEquals("Test Title", alert.getTitle());
        assertEquals("Test Message", alert.getMessage());
    }

    @Test
    public void testGetterAndSetterMethods() {
        alert.setTitle("New Title");
        alert.setMessage("New Message");

        assertEquals("New Title", alert.getTitle());
        assertEquals("New Message", alert.getMessage());
    }

    @Test
    public void testGetCurrentDateTime() {
        // Assuming no currentDateTime is provided in constructor
        assertNotNull(alert.getCurrentDateTime());
    }


}

