package com.example.the_tarlords;

import static org.junit.Assert.assertEquals;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.the_tarlords.data.photo.Photo;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for Photo class
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PhotoTest {
    @Test
    public void testGetCollection() {
        Photo photo = new Photo("testImageData","testCollection", "testName", "testDocId" );
        assertEquals("testCollection", photo.getCollection());
    }
    @Test
    public void testGetName() {
        Photo photo = new Photo("testImageData","testCollection", "testName", "testDocId" );
        assertEquals("testName", photo.getName());
    }
    @Test
    public void testGetDocId() {
        Photo photo = new Photo("testImageData","testCollection", "testName", "testDocId" );
        assertEquals("testDocId", photo.getDocId());
    }
    @Test
    public void testGetImageData() {
        Photo photo = new Photo("testImageData","testCollection", "testName", "testDocId" );
        assertEquals("testImageData", photo.getImageData());
    }
    @Test
    public void testSetCollection() {
        Photo photo = new Photo("testImageData", "testCollection", "testName", "testDocId");
        assertEquals("testCollection", photo.getCollection());
        photo.setCollection("newTestCollection");
        assertEquals("newTestCollection", photo.getCollection());
    }

    @Test
    public void testSetName() {
        Photo photo = new Photo("testImageData", "testCollection", "testName", "testDocId");
        assertEquals("testName", photo.getName());
        photo.setName("newTestName");
        assertEquals("newTestName", photo.getName());
    }

    @Test
    public void testSetDocId() {
        Photo photo = new Photo("testImageData", "testCollection", "testName", "testDocId");
        assertEquals("testDocId", photo.getDocId());
        photo.setDocId("newTestDocId");
        assertEquals("newTestDocId", photo.getDocId());
    }

    @Test
    public void testSetImageData() {
        Photo photo = new Photo("testImageData", "testCollection", "testName", "testDocId");
        assertEquals("testImageData", photo.getImageData());
        photo.setImageData("newTestImageData");
        assertEquals("newTestImageData", photo.getImageData());
    }



}
