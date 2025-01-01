package main.java.services;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Chatbot extends Chatter {
    private static final Logger logger = Logger.getLogger(Chatbot.class.getName());

    public Chatbot() {
        super(getSystemPrompt(), 3, 5);
    }

    private static String getSystemPrompt() {
        return """
            ### System Prompt
            
            You are a helpful assistant that helps users understand project descriptions.
            Your task is to answer the user's questions about a project description.
            Be friendly and clear in your explanations.
            """;
    }

    private static String getUserPrompt(String projectDescription, String userQuestion) {
        return """
            ### User Prompt
            
            Please answer the following questions about the project description:
            Project description:
            %s
            Question:
            %s
            """.formatted(projectDescription, userQuestion);
    }

    public String explainProject(String projectDescription, String userQuestion) throws IOException {
        return chat(getUserPrompt(projectDescription, userQuestion));
    }

    public static void main(String[] args) {
        String projectDescription = """
            This project is a web application that allows users to create, edit, and share documents. 
            Users can register an account, log in, and start creating documents with a rich text editor. 
            Documents can be shared with other users for collaboration. 
            The application also includes a chat feature for real-time communication between users. 
            Users can create multiple documents, organize them into folders, and search for specific documents. 
            The application is built using HTML, CSS, and JavaScript for the frontend, and Node.js for the backend.
            """;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter your question about the project:");
            String userQuestion = scanner.nextLine();

            Chatbot chatbot = new Chatbot();
            String explanation = chatbot.explainProject(projectDescription, userQuestion);
            System.out.println(explanation);
        } catch (IOException e) {
            System.err.println("Error while generating explanation: " + e.getMessage());
        }
    }
}
