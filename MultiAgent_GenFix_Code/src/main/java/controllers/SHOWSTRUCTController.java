package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class SHOWSTRUCTController {
	@FXML
    private MainSideButtonController mainSideButtonController;
    @FXML
    private TextArea coteTextArea;


    public void displaystructure(String code) {
        String newCode = code.replace("\\n", "\n").replace("\\t","\t");
//        System.out.println(newCode);
        coteTextArea.setFont(Font.font("JetBrains Mono", 14));
        coteTextArea.setText(newCode);
    }


    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
    