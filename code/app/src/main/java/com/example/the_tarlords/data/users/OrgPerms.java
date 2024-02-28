package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.event.Event;

public interface OrgPerms {
    Event createEvent(Event event);
    QRCode createQRForCheckIns(QRCode qrCode);

    QRCode reuseQRCode(QR qrCode);
}
