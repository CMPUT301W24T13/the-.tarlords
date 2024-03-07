package com.example.the_tarlords.placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceholderEventContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final ArrayList<PlaceholderEvent> EVENTS = new ArrayList<PlaceholderEvent>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, PlaceholderEvent> ITEM_MAP = new HashMap<String, PlaceholderEvent>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderEvent(i));
        }
    }

    private static void addItem(PlaceholderEvent item) {
        EVENTS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PlaceholderEvent createPlaceholderEvent(int position) {
        return new PlaceholderEvent(String.valueOf(position), "Event " + position, "Attendee",makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("February ").append(position).append(", 2024 @2pm");
        for (int i = 0; i < position; i++) {
            builder.append("");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class PlaceholderEvent {
        public final String id;
        public final String eventTitle;
        public final String userRole;
        public final String date;

        public PlaceholderEvent(String id, String eventTitle, String eventUserStatus, String date) {
            this.id = id;
            this.eventTitle = eventTitle;
            this.userRole = eventUserStatus;
            this.date = date;
        }

        @Override
        public String toString() {
            return eventTitle;
        }
    }
}