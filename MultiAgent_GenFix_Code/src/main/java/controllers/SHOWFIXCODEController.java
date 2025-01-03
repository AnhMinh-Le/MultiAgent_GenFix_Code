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

public class SHOWFIXCODEController {
	@FXML
    private MainSideButtonController mainSideButtonController;
    @FXML
    private TextArea coteTextArea;
    @FXML
    private Button saveButton;

    @FXML
    public void initialize() {
        saveButton.setOnAction(e -> saveCodeToFile());
       
    }

   
    public void displayFixedCode(String code) {
        String newCode = code.replace("\\n", "\n").replace("\\t","\t");
        coteTextArea.setFont(Font.font("JetBrains Mono", 14));
        coteTextArea.setText(newCode);
    }

    private void saveCodeToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
        	    new FileChooser.ExtensionFilter("Java Files", "*.java"),
        	    new FileChooser.ExtensionFilter("Python Files", "*.py")
        	    );
        File saveFile = fileChooser.showSaveDialog(saveButton.getScene().getWindow());

        if (saveFile != null) {
            try (FileWriter writer = new FileWriter(saveFile)) {
                writer.write(coteTextArea.getText());
                showAlert("Code saved successfully to: " + saveFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Failed to save the code.");
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
    