package com.example.gpa_calculator;

import javafx.scene.control.Alert;

public class alertMessage {
    private Alert alert;
    public void errorMessage(String message){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error_Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void successMessage(String message){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information_Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
