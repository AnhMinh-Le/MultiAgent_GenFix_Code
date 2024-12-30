import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class Coder extends Chatter {
    private static final Logger logger = Logger.getLogger(Coder.class.getName());

    public Coder(String language) {
        super(getSystemPrompt(language), 3, 5);
    }

    private static String getSystemPrompt(String language) {
        return """
            ### System Prompt
            
            Your task is to generate the code in %s based on the provided product description.
            The product description includes the requirements for the software system, including the classes, methods, attributes, and their interactions.
            Please follow object-oriented principles and return only the generated code for the requested system.
            Do not provide any explanations, comments, or additional information. Just the code.
            """.formatted(language);
    }

    private static String getUserPrompt(String productDescription, String language) {
        return """
            ### User Prompt
            
            Please generate the code in %s based on the following product description.
            The product description provides the software requirements, including the classes, attributes, methods, and their interactions.
            You need to implement the functionality described in the product description. The software should meet the following requirements:

            - Implement the required classes, attributes, and methods as described in the product description.
            - Ensure the classes interact appropriately according to the described logic.
            - Follow object-oriented principles such as encapsulation, inheritance, and polymorphism where applicable.

            Product Description:

            %s

            Return only the code implementing the described classes, attributes, methods, and logic. Do not include any explanations or comments.
            """.formatted(language, productDescription);
    }

    public String postprocessCodeCompletion(String completion, String lan) {
        int startIndex = completion.indexOf("```" + lan);
        if (startIndex != -1) {
            int endIndex = completion.indexOf("```", startIndex + lan.length() + 3);
            if (endIndex != -1) {
                return completion.substring(startIndex + lan.length() + 3, endIndex).trim();
            }
        } else {
            logger.severe("Error: No code block found");
        }
        return completion;
    }

    public void generateCode(String productDescription, String language, String outputDir) throws IOException {
        String userPrompt = getUserPrompt(productDescription, language);
        String fullResponse = chat(userPrompt);

        if (fullResponse == null || fullResponse.isEmpty()) {
            logger.warning("Received empty or null response from the API.");
            return;
        }

        String code = postprocessCodeCompletion(fullResponse, language);

        if (code.isEmpty()) {
            logger.warning("No code block found in the response.");
            return;
        }

        writeCodeToFile(code, language, outputDir);
    }

    private void writeCodeToFile(String code, String language, String outputDir) throws IOException {
        String fileName = "GeneratedCode." + (language.equals("python") ? "py" : "java");
        File outputFile = new File(outputDir, fileName);

        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            throw new IOException("Failed to create directories.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(code);
        }

        logger.info("Code has been written to: " + outputFile.getAbsolutePath());
    }

    public static void main(String[] args) {
        String outputDirectory = "E:\\Github Repo\\OOP_MultiAgen_GenFix_Code\\data\\generated_code";
        String productDescription = """
            The system is a library management platform designed to handle books, borrowers, and loans. The platform includes the following components:

            1. The `Book` class represents the books in the library.
               - Attributes:
                 - `bookID`: A unique identifier for the book.
                 - `title`: The title of the book.
                 - `author`: The author of the book.
            """;

        try {
            Coder coder = new Coder("java");
            coder.generateCode(productDescription, "java", outputDirectory);
        } catch (IOException e) {
            System.err.println("Error while generating code: " + e.getMessage());
        }
    }
}
