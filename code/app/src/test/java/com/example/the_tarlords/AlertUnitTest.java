package com.example.the_tarlords;

import static org.junit.Assert.assertEquals;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.Alert.Milestone;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AlertUnitTest {

    private Alert mockAlert(){
        String title = "Test title";
        String message = "Test message";
        String currentDateTime = "2024-04-07 04:18:44";
        return new Alert(title,message,currentDateTime);
    }
    private Milestone mockMilestone(){
        String title = "Test milestone title";
        String message = "Test milestone message";
        String count = "1";
        return new Milestone(title,message,count);
    }
    @Test
    public void testTitleSetterAndGetter(){
        Alert alert = mockAlert();
        assertEquals("Test title",alert.getTitle());
        alert.setTitle("Title test");
        assertEquals("Title test",alert.getTitle());

        Milestone milestone = mockMilestone();
        assertEquals("Test milestone title",milestone.getTitle());
        milestone.setTitle("Title milestone test");
        assertEquals("Title milestone test",milestone.getTitle());
    }
    @Test
    public void testMessageSetterAndGetter(){
        Alert alert = mockAlert();
        assertEquals("Test message",alert.getMessage());
        alert.setMessage("Message test");
        assertEquals("Message test",alert.getMessage());

        Milestone milestone = mockMilestone();
        assertEquals("Test milestone message",milestone.getMessage());
        milestone.setMessage("Message milestone test");
        assertEquals("Message milestone test",milestone.getMessage());
    }
    @Test
    public void testCurrentDateTimeSetterAndGetter(){
        Alert alert = mockAlert();
        assertEquals("2024-04-07 04:18:44",alert.getCurrentDateTime());
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        alert.setCurrentDateTime(currentTime);
        assertEquals(currentTime,alert.getCurrentDateTime());

    }
}
