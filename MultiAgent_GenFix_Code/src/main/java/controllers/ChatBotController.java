package main.java.controllers;

import main.java.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatBotController{
    @FXML
    private MainSideButtonController mainSideButtonController;
    @FXML
    private Button browseButton1;
    @FXML
    private TextArea questionArea;
    @FXML
    private TextArea answerArea;
    @FXML
    private Button Askbutton;
    @FXML
    private Label selectedFileLabel1;
    @FXML
    public void initialize() {
        browseButton1.setOnAction(e -> browseFile(selectedFileLabel1));  
        Askbutton.setOnAction(e -> Genanswer());
        
    }
    private void Genanswer() {
        Chatbot chat = new Chatbot();
        try {
            displayanswer(chat.explainProject(openfile(FilePath), questionArea.getText()));
        } catch (IOException ex) {
            Logger.getLogger(ChatBotController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void displayanswer(String answer){
        String finalanswer = answer.replace("\\n", "\n").replace("\\t","\t");
//        System.out.println(newCode);
        answerArea.setWrapText(true);
        answerArea.setText(finalanswer);
    }
    private String FilePath = "";
    private void browseFile(Label label) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(browseButton1.getScene().getWindow());

        if (selectedFile != null) {
        	FilePath = selectedFile.getAbsolutePath();
            label.setText(FilePath);
            showAlert("File selected: " + FilePath);
        }
    }
    private String openfile(String path) throws IOException{
        Path filePath = Path.of(path);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }


    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
