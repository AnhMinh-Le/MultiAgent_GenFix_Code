package main.java.controllers;

import main.java.services.ProductAnalyst;
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

    private Button createprj;


    private String mycode;
    public void initialize() {
    	
//        browseButton1.setOnAction(e -> browseFile(selectedFileLabel1));
        createprj.setOnAction(e -> printstructure());
    }
    private String openfile(String path) throws IOException{
        Path filePath = Path.of(path);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }
    private String output(String description) throws IOException{
        ProductAnalyst code = new ProductAnalyst();
        String finalcode = code.analyzeAndSaveToJson(description)
        return finalcode;
    }
    public void getCode(String code){
        mycode=code;
    }
    private void printstructure(){

        if ( noteTaskDescriptionArea.getText().isEmpty()) {
            showAlert("Please give task description and feedback .");
        } else {
            try {
                Stage currentStage = (Stage) fixbutton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/SHOWSTRUCT.fxml"));
                Stage showGenStage = new Stage();
                showGenStage.setScene(new Scene(loader.load()));
                showGenStage.show();

                SHOWSTRUCTController showstructure = loader.getController();
                String description;
                description = noteTaskDescriptionArea.getText();
                String code = output(description); // Take the generated code
                showstructure.displaystructure(code);

                currentStage.close();  // Close the current stage
            } catch (Exception e) {
                e.printStackTrace();
            }
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
