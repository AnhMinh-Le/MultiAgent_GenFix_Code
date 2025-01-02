package main.java.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import main.resources.data.BankAccount;

public class Tester extends Chatter {
    private static final Logger logger = Logger.getLogger(Tester.class.getName());

    public Tester(String language) {
        super(getSystemPrompt(language), 3, 10);
    }

    private static String getSystemPrompt(String language) {
        return "### System Prompt\n\n"
            + "Your task is to review and provide feedback on a **" + language + "** program. "
            + "Focus on identifying any quality issues in the code and providing recommendations to improve it. ";
    }

    private static String reportAnalysis(String solution, String issueDescription) {
        return "### User Prompt\n\n"
            + "Please review the following code and identify any quality issues. "
            + "After identifying the issues, provide an **issue description** followed by a **fix recommendation**.\n\n"
            + "**Here is the existing code:**\n\n"
            + "```" + solution + "```\n\n"
            + "**Quality issues detected by Static Analysis Tools:**\n\n"
            + "```" + issueDescription + "```\n\n"
            + "Please provide the issue description and fix recommendation in this format:\n\n"
            + "- **Issue Description:** _A detailed description of the code issues and explain their context._\n"
            + "- **Fix Recommendation:** _Your detailed fix recommendation here._\n\n"
            + "**REMEMBER:** You must not generate code.";
    }

    public String generateReport(String solution, String issueDescription) throws IOException {
        String userPrompt = reportAnalysis(solution, issueDescription);
        String fullResponse = chat(userPrompt);

        if (fullResponse == null || fullResponse.isEmpty()) {
            logger.warning("Received empty or null response from the API.");
            return null;
        }
        
        return fullResponse;
    }

    public static void main(String[] args) {
        try {
        	System.out.println("1");
        	StringBuilder solution = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String line;
                // Đọc tất cả các dòng từ Standard Input
                while ((line = reader.readLine()) != null) {
                    solution.append(line).append("\n");
                }
            } catch (IOException e) {
                System.err.println("Error reading input: " + e.getMessage());
                e.printStackTrace();
            }
        	
//        	String solution = """
//        			
//public class BankAccount {
//    private String accountNumber
//    private String accountHolderName;
//    private dou
//    private String accountType;
//
//    public BankAccount(String  String accountHolderName, doubl{
//        this.accountNumber = accountNumber;
//        this.accountHolderName = accountHolderName;
//        this.balance = initialBalance;
//        this.accountType = accountType;
//    }
//
//    public void deposit(double amount) {
//        if (amount > 0)
//            balance += amount;
//        }
//
//
//    public void withdraw(double amount) {
//
//        }
//    }
//
//    public double getBalance() {
//        return balance;
//    }
//
//    public void transferFunds(BankAccount recipient,
//
//        }
//    }
//}
//
//
//        			""";
            
             System.out.println("Solution: " + solution.toString());
            
            File workingDirectory = new File("D:\\MultiAgent_GenFix_Code\\MultiAgent_GenFix_Code\\src\\main\\java\\services");
            File classpathRoot = new File("D:\\MultiAgent_GenFix_Code\\MultiAgent_GenFix_Code\\bin");
            
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", classpathRoot.getAbsolutePath(), "main.java.services.CodeAnalyzer");
            
            processBuilder.directory(workingDirectory);

            processBuilder.environment().putAll(System.getenv());
            
            Process runProcess = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = runProcess.waitFor();

//             System.out.println("Python Output:\n" + output.toString());
//             System.out.println("Exit Code: " + exitCode);

            Tester tester = new Tester("java");
            System.out.println("Check Tester");
            String report = tester.generateReport(solution.toString(), output.toString());

            System.out.println(report);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // try {
        //     Tester tester = new Tester("java");
        //     tester.generateCode(productDescription, "java", outputDirectory);
        // } catch (IOException e) {
        //     System.err.println("Error while generating code: " + e.getMessage());
        // }
    }
}