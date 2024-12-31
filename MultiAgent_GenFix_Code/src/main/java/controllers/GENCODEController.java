package main.java.controllers;

import main.java.services.Coder;
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

public class GENCODEController {

    @FXML
    private Button browseButton1;
    @FXML
    private Button browseButton2;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private Button generateButton;
    @FXML
    private Label selectedFileLabel1;
    @FXML
    private Label selectedFileLabel2; 
    @FXML
    private Button gencodebutton;
    @FXML
    private Button fixcodebutton;
    @FXML
    private ChoiceBox<String> choicebox;
    private final String[] language ={"java","python","C","C++"};
    private String myLanguage;
    public void initialize() {
        choicebox.getItems().addAll(language);
        choicebox.setOnAction(e -> getlanguage());
        gencodebutton.setOnAction(e -> opengencode());
        fixcodebutton.setOnAction(e -> openfixcode());
        browseButton1.setOnAction(e -> browseFileUML(selectedFileLabel1));
        browseButton2.setOnAction(e -> browseFileTD(selectedFileLabel2));
        generateButton.setOnAction(e -> printgenerateCode());
        
    }
    private void getlanguage(){
        myLanguage = choicebox.getValue();
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
    private String UMLFilePath;
    private void browseFileUML(Label label) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(browseButton1.getScene().getWindow());

        if (selectedFile != null) {
            UMLFilePath = selectedFile.getAbsolutePath();
            label.setText(UMLFilePath);
//            label.setText(selectedFile.getName());
            showAlert("File selected: " + UMLFilePath);
        }
    }
    private String TDFilePath;
    private void browseFileTD(Label label) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(browseButton2.getScene().getWindow());

        if (selectedFile != null) {
        	TDFilePath = selectedFile.getAbsolutePath();
            label.setText(TDFilePath);
//            label.setText(selectedFile.getName());
            showAlert("File selected: " + TDFilePath);
        }
    }
    private String openfile(String path) throws IOException{
        Path filePath = Path.of(path);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }
    private String generatecode(String language) throws IOException{
        Coder code = new Coder(language);
        String Description = openfile(TDFilePath);
        String GeneratedCode = code.generateCode(Description, language);
        return GeneratedCode;
    }
    private void printgenerateCode() {
        String file1 = selectedFileLabel1.getText();
        String file2 = selectedFileLabel2.getText();

        if ((file1.isEmpty() || file1.equals("No file selected")) &&
            (file2.isEmpty() || file2.equals("No file selected"))) {
            showAlert("Please select at least one file (UML or Task Description).");
        } else {
            try {
                Stage currentStage = (Stage) generateButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/SHOWGENCODE.fxml"));
                Stage showGenStage = new Stage();
                showGenStage.setScene(new Scene(loader.load()));
                showGenStage.show();

                SHOWGENCODEController showGenController = loader.getController();
                String code = generatecode(myLanguage); // Take the generated code
                showGenController.displayGeneratedCode(code);

                currentStage.close();  // Close the current stage
            } catch (Exception e) {
                e.printStackTrace();
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
