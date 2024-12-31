package main.java.controllers;

import main.java.services.Fixer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Alert;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
public class FIXCODEController2 {
    @FXML
    private Button browseButton1;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private Button fixbutton;
    @FXML
    private Label selectedFileLabel1;
    @FXML
    private Button gencodebutton;
    @FXML
    private Button fixcodebutton;
    private String TDFilePath;
    @FXML
    public void initialize() {
        gencodebutton.setOnAction(e -> opengencode());
        fixcodebutton.setOnAction(e -> openfixcode());
        browseButton1.setOnAction(e -> browseFile(selectedFileLabel1));
        fixbutton.setOnAction(e -> printfixedcode());
    }
    private String openfile(String path) throws IOException{
        Path filePath = Path.of(path);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }
    private String fixcode(String language) throws IOException{
        Fixer code = new Fixer(language);
        String Description = openfile(TDFilePath);
        String GeneratedCode = code.generateCode(Description, language);
        return GeneratedCode;
    }
    private void printfixedcode(){
        String file1 = selectedFileLabel1.getText();

        if ((file1.isEmpty() || file1.equals("No file selected")) && noteTextArea.getText().isEmpty()) {
            showAlert("Please select at least one file .");
        } else {
            try {
                Stage currentStage = (Stage) fixbutton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/SHOWFIXCODE.fxml"));
                Stage showGenStage = new Stage();
                showGenStage.setScene(new Scene(loader.load()));
                showGenStage.show();

                SHOWGENCODEController showGenController = loader.getController();
                String code = fixcode("java"); // Take the generated code
                showGenController.displayGeneratedCode(code);

                currentStage.close();  // Close the current stage
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void opengencode() {
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
    
    private void openfixcode() {
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
    private void browseFile(Label label) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(browseButton1.getScene().getWindow());

        if (selectedFile != null) {
            TDFilePath = selectedFile.getAbsolutePath();
            label.setText(selectedFile.getName());
            showAlert("File selected: " + TDFilePath);
        }
    }
    
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
