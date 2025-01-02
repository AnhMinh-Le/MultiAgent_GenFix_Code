package main.java.services;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.util.logging.Logger;
import java.io.IOException;

public class Chatter {

    private static final Logger logger = Logger.getLogger(Chatter.class.getName());
    private final int retries;
    private final int timeout;
    private final String systemMessage;
    private final String apiKey;

    public Chatter(String systemMessage, int retries, int timeout) {
        this.retries = retries;
        this.timeout = timeout;
        this.systemMessage = systemMessage;

        apiKey = ".";
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key not found. Please set the OPENAI_API_KEY environment variable.");
        }
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    public static String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input // Escape backslashes
                .replace("\"", "\\\"") // Escape double quotes
                .replace("\n", "\\n")  // Escape newlines
                .replace("\r", "")     // Remove carriage returns
                .replace("\t", "\\t"); // Escape tabs
    }

    private String callOpenAiApi(String query) {
        for (int attempt = 0; attempt < retries; attempt++) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("https://api.openai.com/v1/chat/completions");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + apiKey);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(timeout * 1000);
                connection.setReadTimeout(timeout * 1000);
                connection.setDoOutput(true);

                String jsonInputString = """
                    {
                        "model": "gpt-3.5-turbo-1106",
                        "messages": [
                            {
                                "role": "system",
                                "content": "%s"
                            },
                            {
                                "role": "user",
                                "content": "%s"
                            }
                        ],
                        "temperature": %.1f
                    }
                """.formatted(escapeJson(systemMessage), escapeJson(query), 0.7 + attempt * 0.1);

                // Debug JSON payload
                System.out.println("JSON Payload: " + jsonInputString);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        return response.toString();
                    }
                } else {
                    logger.severe("[Chatter] - Failed request. HTTP response code: " + responseCode);
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                        StringBuilder errorResponse = new StringBuilder();
                        String errorLine;
                        while ((errorLine = br.readLine()) != null) {
                            errorResponse.append(errorLine.trim());
                        }
                        logger.severe("[Chatter] - Error response: " + errorResponse);
                    }
                }
            } catch (Exception e) {
                logger.severe("[Chatter] - An exception occurred. " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        return "";
    }

    public String chatRaw(String query) {
        return callOpenAiApi(query);
    }

    private String extractContentFromJson(String json) {
        String marker = "\"content\": \"";
        int startIndex = json.indexOf(marker);
        if (startIndex != -1) {
            startIndex += marker.length();
            int endIndex = json.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return json.substring(startIndex, endIndex);
            }
        }
        return ""; // Return empty if content is not found
    }

    public String chat(String query) {
        String response = chatRaw(query);
        return extractContentFromJson(response);
    }

    public static void main(String[] args) {
        Chatter chatter = new Chatter("You are an assistant who helps with Python programming.", 3, 5);
        String response = chatter.chat("How do I create a list encompassing dictionaries in Python?");
        System.out.println("Response from API: " + response);
    }
}
