package main.java.services;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class Translator extends Chatter {
    private static final String TRANSLATION_SYSTEM_MESSAGE = "You are a translator. Translate the following text to English.";

    public Translator() {
        super(TRANSLATION_SYSTEM_MESSAGE, 3, 10);
    }

    public String translate(String text) {
        String response = chat("Translate the following text to English: " + text);
        if (response != null && !response.isEmpty()) {
            int contentStart = response.indexOf("\"content\":\"");
            if (contentStart != -1) {
                int start = contentStart + 11;
                int end = response.indexOf("\"", start);
                if (end != -1) {
                    return response.substring(start, end).replace("\\n", "\n");
                }
            }
        }
        return "Translation failed";
    }

    public static void main(String[] args) {
        Translator translator = new Translator();
        String textToTranslate = "Bonjour le monde";
        String translatedText = translator.translate(textToTranslate);
        System.out.println("Original text: " + textToTranslate);
        System.out.println("Translated text: " + translatedText);
    }
}