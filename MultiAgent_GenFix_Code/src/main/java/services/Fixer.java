package main.java.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.OutputStream;
import java.io.InputStream;

public class Fixer extends Coder {
    private static final Logger logger = Logger.getLogger(Fixer.class.getName());
    private final String language;

    public Fixer(String language) {
        super(language);
        this.language = language.toLowerCase();
        if (!language.equalsIgnoreCase("java") && !language.equalsIgnoreCase("python")) {
            throw new IllegalArgumentException("Unsupported language. Only 'java' and 'python' are supported.");
        }
    }

    private String getFixPrompt(String code, String taskDescription, String report, String staticAnalysisErrors) {
        String codeBlock = getLanguageCodeBlock(code);
        String languageInfo = "Please fix the " + language + " code";
        
        // Case 1: No task description, no report
        if (isNullOrEmpty(taskDescription) && isNullOrEmpty(report)) {
            return """
                ### Fix Code Prompt
                Your task is to fix the %s code.
                Based on the following Issue Description and Fix recommendation:
                %s
                
                Review and fix the following code:
                %s

                Return only the fixed code without any explanations.
                """.formatted(language, staticAnalysisErrors, codeBlock);
        }
        
        // Case 2: Has task description, no report
        if (!isNullOrEmpty(taskDescription) && isNullOrEmpty(report)) {
            return """
                ### Fix Code Prompt
                Your task is to fix the %s code.
                According to this task description:
                
                Task Description:
                %s

                And based on the following Issue Description and Fix recommendation:
                %s
                
                Review and fix the following code:
                %s

                Return only the fixed code without any explanations.
                """.formatted(language, taskDescription, staticAnalysisErrors, codeBlock);
        }

        // Case 3: No task description, has report
        if (isNullOrEmpty(taskDescription) && !isNullOrEmpty(report)) {
            return """
                ### Fix Code Prompt
                Your task is to fix the %s code.
                Based on the report of user:
                %s

                And the following Issue Description and Fix recommendation:
                %s
                
                Review and fix the following code:
                %s

                Return only the fixed code without any explanations.
                """.formatted(language, report, staticAnalysisErrors, codeBlock);
        }
        
        // Case 4: Has all two
        return """
            ### Fix Code Prompt
            Your task is to fix the %s code.
            Based on all available information:
            
            Task Description:
            %s
            
            Reported Issues:
            %s
            
            Static Analysis Errors:
            %s
            
            Code to fix:
            %s

            Return only the fixed code without any explanations.
            """.formatted(language, taskDescription, report, staticAnalysisErrors, codeBlock);
    }

    private String getLanguageCodeBlock(String code) {
        return "```" + language + "\n" + code + "\n```";
    }

    public String fixCode(String code, String taskDescription, String report, String staticAnalysisErrors) throws IOException {
        String fixPrompt = getFixPrompt(code, taskDescription, report, staticAnalysisErrors);
        String fullResponse = chat(fixPrompt);
        return postprocessCodeCompletion(fullResponse, language);
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public String getStaticAnalysisErrors(String code) {
        try {
            Path dataDir = Paths.get("../resources/data");
            try {
                if (Files.exists(dataDir) && Files.isDirectory(dataDir)) {
                    Files.walk(dataDir)
                        .filter(Files::isRegularFile)
                        .forEach(file -> {
                            try {
                                Files.delete(file);
                                System.out.println("Deleted file: " + file);
                            } catch (IOException e) {
                                System.err.println("Failed to delete file: " + file);
                            }
                        });
                } else {
                    System.out.println("Directory ../resources/data does not exist or is not a directory.");
                }
            } catch (IOException e) {
                System.err.println("Error accessing directory: " + e.getMessage());
            }

            String fileName = language.equals("java") ? "code.java" : "code.py";
            Path filePath = dataDir.resolve(fileName);

            try {
                Files.createDirectories(dataDir); // Tạo thư mục nếu chưa tồn tại
                Files.writeString(filePath, code);
                System.out.println("Created file: " + filePath);
            } catch (IOException e) {
                System.err.println("Failed to create file: " + e.getMessage());
            }
            ProcessBuilder compile = new ProcessBuilder("javac", "Tester.java", "Fixer.java", "Chatter.java");
            compile.start();
            ProcessBuilder pb = new ProcessBuilder("java", "Tester.java");
            Process process = pb.start();

            OutputStream os = process.getOutputStream();
            PrintWriter writer = new PrintWriter(os, true);
            writer.println(code);
            writer.close();

            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            String staticAnalysisErrors = "";
            while ((line = reader.readLine()) != null) {
                staticAnalysisErrors = line;
                // System.out.println("Static Analysis: " + staticAnalysisErrors); 
            }
            // System.out.println("Static Analysis: " + staticAnalysisErrors);

            return staticAnalysisErrors;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("File reading error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
        return "";
    }

    // public static void main(String[] args) {
    //     Fixer fixer = new Fixer("java");
    //     String statics = fixer.getStaticAnalysisErrors("");
    //     // System.out.println(statics);
    // }
}