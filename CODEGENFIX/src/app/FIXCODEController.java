package app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.File;
import javafx.scene.control.Alert;
public class FIXCODEController {

    @FXML
    private Button browseButton1;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private Button nextButton;
    @FXML
    private Label selectedFileLabel1;
    @FXML
    private Button gencodebutton;
    @FXML
    private Button fixcodebutton;

    @FXML
    public void initialize() {
        gencodebutton.setOnAction(e -> opengencode());
        fixcodebutton.setOnAction(e -> openfixcode());
        browseButton1.setOnAction(e -> browseFileCode(selectedFileLabel1));
        nextButton.setOnAction(e -> opennextpage());
    }
    private void opennextpage(){
        String file1 = selectedFileLabel1.getText();
        if ((file1.isEmpty() || file1.equals("No file selected")) && noteTextArea.getText().isEmpty()){
            showAlert("Please select at least a code to import");
            return;
        }
        try {
            Stage currentStage = (Stage) nextButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/codegenfix/FIXCODE2.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/codegenfix/GENCODE.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/codegenfix/FIXCODE.fxml"));
            Stage showGenStage = new Stage();
            showGenStage.setScene(new Scene(loader.load()));
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
