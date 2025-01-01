package main.java.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainSideButtonController {
	@FXML
    protected Button gencodebutton;
    @FXML
    protected Button fixcodebutton;
    @FXML
    protected Button projectstructbutton;
    private void openstructure() {
        try {
            Stage currentStage = (Stage) gencodebutton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/PRJSTRUCT.fxml"));
            Stage showGenStage = new Stage();
            showGenStage.setScene(new Scene(loader.load()));
            showGenStage.show();
            currentStage.close();  // Close the current stage
        } catch (Exception e) {
            e.printStackTrace();
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
    public void initialize() {
    	gencodebutton.setOnAction(e -> opengencode());
        fixcodebutton.setOnAction(e -> openfixcode());
        projectstructbutton.setOnAction(e -> openstructure());
    }

}
