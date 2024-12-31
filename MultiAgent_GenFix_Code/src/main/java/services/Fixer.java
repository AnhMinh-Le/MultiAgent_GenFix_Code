package main.java.services;

import java.io.IOException;
import java.util.logging.Logger;

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
        String languageInfo = "Please fix this " + language + " code";
        
        // Case 1: No task description, no report, no static analysis errors
        if (isNullOrEmpty(taskDescription) && isNullOrEmpty(report) && isNullOrEmpty(staticAnalysisErrors)) {
            return """
                ### Fix Code Prompt
                %s. Review and fix any potential issues in the following code:
                
                %s
                Return only the fixed code without any explanations.
                """.formatted(languageInfo, codeBlock);
        }
        
        // Case 2: Has task description, no report, no static analysis errors
        if (!isNullOrEmpty(taskDescription) && isNullOrEmpty(report) && isNullOrEmpty(staticAnalysisErrors)) {
            return """
                ### Fix Code Prompt
                %s according to this task description:
                
                Task Description:
                %s
                
                Code to fix:
                %s
                Return only the fixed code without any explanations.
                """.formatted(languageInfo, taskDescription, codeBlock);
        }
        
        // Case 5: Has all three
        return """
            ### Fix Code Prompt
            %s based on all available information:
            
            Task Description:
            %s
            
            Reported Issues:
            %s
            
            Static Analysis Errors:
            %s
            
            Code to fix:
            %s
            Return only the fixed code without any explanations.
            """.formatted(languageInfo, taskDescription, report, staticAnalysisErrors, codeBlock);
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
}
