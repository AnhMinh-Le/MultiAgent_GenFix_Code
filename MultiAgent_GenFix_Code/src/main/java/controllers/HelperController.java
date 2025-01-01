package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HelperController {

    @FXML
    private Hyperlink visitDocumentation;

    @FXML
    private Hyperlink contactSupport;

    @FXML
    public void initialize() {
        // Set actions for the hyperlinks
        visitDocumentation.setOnAction(e -> openLink("https://documentation.example.com"));
        contactSupport.setOnAction(e -> openLink("https://support.example.com"));
    }

    private void openLink(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                // Handle exceptions by logging or showing a message to the user
            }
        } else {
            System.err.println("Desktop is not supported. Cannot open links.");
        }
    }
}
