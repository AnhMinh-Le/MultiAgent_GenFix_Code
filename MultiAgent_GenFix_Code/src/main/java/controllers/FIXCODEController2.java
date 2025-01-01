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
    private MainSideButtonController mainSideButtonController;
    @FXML
    private TextArea noteTaskDescriptionArea;
    @FXML
    private TextArea noteFeedBackArea;
    @FXML
    private Button fixbutton;


    private String mycode;
    private String mylanguage;
    public void initialize() {
    	
//        browseButton1.setOnAction(e -> browseFile(selectedFileLabel1));
        fixbutton.setOnAction(e -> printfixedcode());
    }
    private String openfile(String path) throws IOException{
        Path filePath = Path.of(path);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }
    private String printfixcode(String inputcode, String taskDescription, String report, String staticAnalysisErrors) throws IOException{
        Fixer code = new Fixer(mylanguage);
        String finalcode = code.fixCode(inputcode, taskDescription, report, staticAnalysisErrors);
        return finalcode;
    }
    public void getCode(String code){
        mycode=code;
    }
    public void getlanguage(String language){
        mylanguage = language;
    }
    private void printfixedcode(){

        if ( noteFeedBackArea.getText().isEmpty() && noteTaskDescriptionArea.getText().isEmpty()) {
            showAlert("Please give task description and feedback .");
        } else {
            try {
                Stage currentStage = (Stage) fixbutton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/SHOWFIXCODE.fxml"));
                Stage showGenStage = new Stage();
                showGenStage.setScene(new Scene(loader.load()));
                showGenStage.show();

                SHOWFIXCODEController showFixController = loader.getController();
                String description,feedback;
                description =noteTaskDescriptionArea.getText();
                feedback=noteFeedBackArea.getText();
                String code = printfixcode(mycode,description,feedback,""); // Take the generated code
                showFixController.displayFixedCode(code);

                currentStage.close();  // Close the current stage
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


   
//    private void browseFile(Label label) {
//        FileChooser fileChooser = new FileChooser();
//        File selectedFile = fileChooser.showOpenDialog(browseButton1.getScene().getWindow());
//
//        if (selectedFile != null) {
//            TDFilePath = selectedFile.getAbsolutePath();
//            label.setText(selectedFile.getName());
//            showAlert("File selected: " + TDFilePath);
//        }
//    }
//    
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
