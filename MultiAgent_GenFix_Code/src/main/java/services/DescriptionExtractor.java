package main.java.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.io.File;

public class DescriptionExtractor extends Chatter {

    public DescriptionExtractor() {
        super("You are an assistant who helps analyze UML diagrams and convert them to textual descriptions", 3, 10);
    }

    public void extractAndSaveDescription(String imagePath, String outputPath) {
        try {
            // Initialize Tesseract OCR
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // Set path to Tesseract data
          
            // Extract text from image
            String extractedText = tesseract.doOCR(new File(imagePath));

            // Create prompt for analyzing the UML
            String prompt = """
                Given the following UML diagram text, generate a detailed description of the system architecture.
                Focus on:
                - Class relationships
                - Methods and attributes
                - System structure
                - Design patterns used (if any)
                
                UML Text:
                """ + extractedText;

            // Send prompt to the Chat API
            String response = chat(prompt);

            // Save the description to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                writer.write(response);
            }

        } catch (TesseractException e) {
            System.err.println("Error during OCR: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
    	
        DescriptionExtractor extractor = new DescriptionExtractor();
        String imagePath = "D:/HUST/Test.jpg";
        String outputPath = "D:/HUST/testdes.txt";
        
        extractor.extractAndSaveDescription(imagePath, outputPath);
    }
}