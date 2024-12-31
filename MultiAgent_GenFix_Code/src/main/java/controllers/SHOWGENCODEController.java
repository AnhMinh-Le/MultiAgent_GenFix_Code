package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class SHOWGENCODEController {

    @FXML
    private TextArea coteTextArea;
    @FXML
    private Button saveButton;
    @FXML
    private Button gencodebutton;
    @FXML
    private Button fixcodebutton;

    @FXML
    public void initialize() {
        saveButton.setOnAction(e -> saveCodeToFile());
        gencodebutton.setOnAction(e -> opengencode());
        fixcodebutton.setOnAction(e -> openfixcode());
    }
    private void opengencode(){
        try {
                Stage currentStage = (Stage) gencodebutton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/GENCODE.fxml"));
                Stage showGenStage = new Stage();
                showGenStage.setScene(new Scene(loader.load()));
                showGenStage.show();
                currentStage.close();  // Close the current stage
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    private void openfixcode(){
       try {
                Stage currentStage = (Stage) fixcodebutton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/FIXCODE.fxml"));
                Stage showGenStage = new Stage();
                showGenStage.setScene(new Scene(loader.load()));
                showGenStage.show();
                currentStage.close();  // Close the current stage
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    public void displayGeneratedCode(String code) {
        String newCode = code.replace("\\n", "\n").replace("\\t","\t");
//        System.out.println(newCode);
        coteTextArea.setText(newCode);
    }

    private void saveCodeToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
        	    new FileChooser.ExtensionFilter("Java Files", "*.java"),
        	    new FileChooser.ExtensionFilter("Python Files", "*.py"),
        	    new FileChooser.ExtensionFilter("C Files", "*.c"),
        	    new FileChooser.ExtensionFilter("C++ Files", "*.cpp"));
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
    