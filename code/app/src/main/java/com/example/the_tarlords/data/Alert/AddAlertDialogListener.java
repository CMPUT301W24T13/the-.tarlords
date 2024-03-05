package com.example.the_tarlords.data.Alert;

public interface AddAlertDialogListener {

    void addAlert(Alert alert);
    void editAlert(Alert oldAlert, String newTitle, String newMessage);

    void deleteAlert(Alert alert);

}