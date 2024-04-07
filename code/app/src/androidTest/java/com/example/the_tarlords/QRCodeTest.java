package com.example.the_tarlords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.the_tarlords.data.QR.QRCode;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QRCodeTest {
    @Test
    public void testSetAndGetId() {
        QRCode qr = new QRCode();
        String id = "123";
        qr.setQrID("123");
        assertEquals(id, qr.getQrID());
    }

    @Test
    public void testMakeNewDocID() {
        QRCode qr = new QRCode();
        qr.makeNewDocID();
        assertNotNull(qr.getQrID());
    }

    @Test
    public void testpastEventwithPastEvent() {
        QRCode qr = new QRCode();
        assertTrue(qr.pastEvent("Apr 06, 2024", "10:00 AM"));
    }

    @Test
    public void testpastEventwithFutureEvent() {
        QRCode qr = new QRCode();
        assertFalse(qr.pastEvent("Apr 30, 2024", "10:00 AM"));
    }
}
