package main.java.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

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
            + "**REMEMBER:** No code generated.";
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
        // String outputDirectory = "E:\\Github Repo\\OOP_MultiAgen_GenFix_Code\\data\\generated_code";
        // String productDescription = """
        //     The system is a library management platform designed to handle books, borrowers, and loans. The platform includes the following components:

        //     1. The `Book` class represents the books in the library.
        //        - Attributes:
        //          - `bookID`: A unique identifier for the book.
        //          - `title`: The title of the book.
        //          - `author`: The author of the book.
        //     """;

        try {
            String line;
            BufferedReader readerFixer = new BufferedReader(new InputStreamReader(System.in));
            StringBuilder solution = new StringBuilder();
            while ((line = readerFixer.readLine()) != null) {
                solution.append(line).append("\n");
            }
            // System.out.println("Solution: " + solution.toString());

            // Lệnh để chạy Python script
            String command = "python ../../python/services/analysis_by_command.py"; // Thay "python" bằng "python3" nếu cần
            
            // Thực thi lệnh
            @SuppressWarnings("deprecation")
			Process process = Runtime.getRuntime().exec(command);

            // Đọc output từ Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Đợi process hoàn tất và lấy mã thoát
            int exitCode = process.waitFor();

            // System.out.println("Python Output:\n" + output.toString());
            // System.out.println("Exit Code: " + exitCode);

            Tester tester = new Tester("java");
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