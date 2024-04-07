package com.example.the_tarlords;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.the_tarlords.data.users.User;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserTest {

    @Test
    public void testSetFirstName() {
        User user = new User("TestId", "TestFirstName", "TestLastName", "7809999999", "test@gmail.com");
        user.setFirstName("TestFirstName2");
        assertEquals("TestFirstName2", user.getFirstName());
    }

}
