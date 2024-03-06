package com.example.the_tarlords.data.Alert;

public interface AddAlertDialogListener {
    /**
     * adds an alert to the adapter
     * @param alert --> alert object
     */
    void addAlert(Alert alert);

    /**
     * edits an alert with new title and new message
     * @param oldAlert --> alert to be editted
     * @param newTitle
     * @param newMessage
     */
    void editAlert(Alert oldAlert, String newTitle, String newMessage);

    /**
     * deletes the alert given
     * @param alert
     */
    void deleteAlert(Alert alert);

}