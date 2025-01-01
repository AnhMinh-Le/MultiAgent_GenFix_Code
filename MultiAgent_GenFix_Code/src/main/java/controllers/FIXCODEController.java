package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.scene.control.Alert;
public class FIXCODEController{
	@FXML
    private MainSideButtonController mainSideButtonController;
    @FXML
    private ChoiceBox<String> choicebox;
    private final String[] language ={"JAVA","PYTHON"};
    private String myLanguage;
    @FXML
    private Button browseButton1;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private Button nextButton;
    @FXML
    private Label selectedFileLabel1;

    @FXML
    public void initialize() {
    	
        choicebox.getItems().addAll(language);
        choicebox.setOnAction(e -> getlanguage());

        browseButton1.setOnAction(e -> browseFileCode(selectedFileLabel1));
        nextButton.setOnAction(e -> opennextpage());
    }
    private void getlanguage(){
        myLanguage = choicebox.getValue();
    }
    private String openfile(String path) throws IOException{
        Path filePath = Path.of(path);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }
    private void opennextpage(){
        String file1 = selectedFileLabel1.getText();
        if ((file1.isEmpty() || file1.equals("No file selected")) && noteTextArea.getText().isEmpty()){
            showAlert("Please select at least a code to import");
            return;
        }
        try {

            Stage currentStage = (Stage) nextButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/FIXCODE2.fxml"));
            Stage showGenStage = new Stage();
            showGenStage.setScene(new Scene(loader.load()));            
            FIXCODEController2 fixcode2 = loader.getController();
            if (noteTextArea.getText().isEmpty() == false){
                fixcode2.getCode(noteTextArea.getText());
            }else{
                fixcode2.getCode(openfile(CodeFilePath));
            }
            fixcode2.getlanguage(myLanguage);
            showGenStage.show();
            currentStage.close();  // Close the current stage
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String CodeFilePath;
    private void browseFileCode(Label label) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(browseButton1.getScene().getWindow());
        if (selectedFile != null) {
            CodeFilePath = selectedFile.getAbsolutePath();
            label.setText(selectedFile.getName());
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
