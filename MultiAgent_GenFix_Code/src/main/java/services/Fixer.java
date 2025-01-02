package main.java.services;

import java.io.BufferedReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

import main.resources.data.BankAccount;

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
    
    public static String getJavaFileName(String javaCode) {
        // Biểu thức chính quy để tìm lớp public
        String regex = "public\\s+class\\s+(\\w+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(javaCode);
        
        if (matcher.find()) {
            String className = matcher.group(1);
            return className + ".java";
        } else {
            // Nếu không tìm thấy lớp public, có thể kiểm tra các lớp khác hoặc xử lý theo yêu cầu
            return "Không tìm thấy lớp public trong đoạn mã.";
        }
    }

    public String getStaticAnalysisErrors(String code) {
        try {
            Path dataDir = Paths.get("D:\\MultiAgent_GenFix_Code\\MultiAgent_GenFix_Code\\src\\main\\resources\\data");
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
                    System.out.println("Directory /main/resources/data does not exist or is not a directory.");
                }
            } catch (IOException e) {
                System.err.println("Error accessing directory: " + e.getMessage());
            }

            String fileName = language.equals("java") ? getJavaFileName(code) : "inputcode.py";
            Path filePath = dataDir.resolve(fileName);

            try {
                Files.createDirectories(dataDir); // Tạo thư mục nếu chưa tồn tại
                Files.writeString(filePath, code);
                System.out.println("Created file: " + filePath);
            } catch (IOException e) {
                System.err.println("Failed to create file: " + e.getMessage());
            }
            System.out.println(code);
            
            File workingDirectory = new File("D:\\MultiAgent_GenFix_Code\\MultiAgent_GenFix_Code\\src\\main\\java\\services");
            File classpathRoot = new File("D:\\MultiAgent_GenFix_Code\\MultiAgent_GenFix_Code\\bin");
            
            ProcessBuilder compile = new ProcessBuilder("javac", "Tester.java");
            compile.directory(workingDirectory).start().waitFor();

            ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpathRoot.getAbsolutePath(), "main.java.services.Tester");
            pb.directory(workingDirectory);
            Process process = pb.start();
            
            try (OutputStream os = process.getOutputStream();
                PrintWriter writer = new PrintWriter(os, true)) {
                writer.println(code);
                // Đóng writer để báo hiệu kết thúc dữ liệu
                writer.close();
            }

            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
            
            System.out.println("Hello2");

            String line;
            String staticAnalysisErrors = "";
            
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder output = new StringBuilder();
            StringBuilder errors = new StringBuilder();

            // Đọc stdout
            while ((line = stdOut.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Đọc stderr
            while ((line = stdErr.readLine()) != null) {
                errors.append(line).append("\n");
            }

            // Chờ tiến trình hoàn thành
            int exitCode = process.waitFor();
            
            System.out.println("Output:\n" + output.toString());
            System.out.println("Errors:\n" + errors.toString());
//            while ((line = reader.readLine()) != null) {
//                staticAnalysisErrors = line;
//                 System.out.println("Static Analysis: " + staticAnalysisErrors); 
//            }
//            System.out.println("Static Analysis: " + staticAnalysisErrors);
//            
//            System.out.println("Hello3");

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

    public static void main(String[] args) {
        Fixer fixer = new Fixer("java");
        String statics = fixer.getStaticAnalysisErrors("""
        		
public class BankAccount {
    private String accountNumber
    private String accountHolderName;
    private dou
    private String accountType;

    public BankAccount(String  String accountHolderName, doubl{
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.accountType = accountType;
    }

    public void deposit(double amount) {
        if (amount > 0) 
            balance += amount;
        }
    

    public void withdraw(double amount) {

        }
    }

    public double getBalance() {
        return balance;
    }

    public void transferFunds(BankAccount recipient,
        
        }
    }
}

        		""");
        // System.out.println(statics);
    }
}