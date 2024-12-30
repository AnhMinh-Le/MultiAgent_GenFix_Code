package app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.File;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

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
    public void initialize() {
        gencodebutton.setOnAction(e -> opengencode());
        fixcodebutton.setOnAction(e -> openfixcode());
        browseButton1.setOnAction(e -> browseFileUML(selectedFileLabel1));
        browseButton2.setOnAction(e -> browseFileTD(selectedFileLabel2));
        generateButton.setOnAction(e -> printgenerateCode());
    }
        
    private void opengencode(){
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
    private void openfixcode(){
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
    private String generatecode(){
        String GeneratedCode = """
            // Example Code
            public class Test {
                public static void main(String[] args) {
                    System.out.println("Generated code for testing purposes.");
                }
            }
        """;
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/codegenfix/SHOWGENCODE.fxml"));
                Stage showGenStage = new Stage();
                showGenStage.setScene(new Scene(loader.load()));
                showGenStage.show();

                SHOWGENCODEController showGenController = loader.getController();
                String code = generatecode(); // Take the generated code
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
