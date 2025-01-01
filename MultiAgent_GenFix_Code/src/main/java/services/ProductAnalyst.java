package main.java.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ProductAnalyst extends Chatter {

  public ProductAnalyst() {
      super("You are an assistant who helps create project's repository structure", 3, 10);
  }

  public String analyzeAndSaveToJson(String description) {
      String prompt = """
          Given a product description, generate a repository's structure.
          Show the structure using ascii art with proper indentation using │, ├, └, and ─ characters.
          Include:
          - Source code directory (src) with proper package structure based on features
          - Test directory
          - Resource files
          - Configuration files
          - Documentation
          
          Here is the product description:
          """ + description ;

      // Send prompt to the Chat API
      String response = chat(prompt);
      
      // Clean and print the structure
      String cleanedStructure = postprocessCodeCompletion(response);
      System.out.println("\nProject Structure for AIMS:");
      System.out.println("========================\n");
      System.out.println(cleanedStructure);
      return cleandedStructure;
//      saveToFile(cleanedStructure, outputPath);
  }
  public String postprocessCodeCompletion(String completion) {
    int startIndex = completion.indexOf("```");
    if (startIndex != -1) {
        int endIndex = completion.indexOf("```", startIndex + 3);
        if (endIndex != -1) {
            return completion.substring(startIndex + 3, endIndex).trim();
        }
    } else {
        System.err.println("Error: No code block found");
    }
    return completion;
  }

  public void saveToFile(String content, String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write(content);
        System.out.println("Structure successfully saved to: " + filePath);
    } catch (IOException e) {
        System.err.println("Error saving structure to file: " + e.getMessage());
    }
  }

  

  public static void main(String[] args) {
      ProductAnalyst analyst = new ProductAnalyst();
      String description = """
          There might be a future that Tiki and Sendo be in talks over a potential merger to contend other e-commerce platforms and especially those who have foreign backers. The merger of these two firms would create a Ti-do company, where "Ti" is from Tiki, and "do" is from Sendo, which means a billion-dollar company in Vietnamese. That firm, Ti-do company, would like you to help them create a brand-new system for AIMS project (AIMS stands for An Internet Media Store). Currently, there is only one type of media: Digital Video Disc (DVD). Customers can browse the list of DVDs available in the store, the display order is based on their added date, from latest to oldest. When a customer wants to search for DVDs to add to cart, he or she can choose one of three searching options. The software will display a list of all matches (latest DVDs first) with all their information. He or she can also choose to play a specific DVD. The software will play a DVD (a demo part). If a DVD has the length 0 or less, the system must notify the customer that the DVD cannot be played. o When a customer searches for DVDs by title, he or she provides a string of keywords. If any DVD has the title containing any word in the string of keywords, it is counted as a match. Note that the comparison of words here is case-insensitive. o When a customer searches for DVDs by category, he or she provides the category name. If any DVD has the matching category (case-insensitive), it is counted as a match. o When a customer searches for DVDs by price, he or she provides either the minimum and maximum cost, or just the maximum cost. Customers can view the detail information of a DVD from the list of DVDs. He/she can add a DVD to a cart from a list of DVDs or the detail screen. When a customer wants to see the current cart, the system displays all the information of the DVDs, along with the total cost. Customers may listen to a DVD (a demo part) in the cart before confirming to place an order. Customers can sort all DVDs in the cart by title or by cost: o Sort by title: the system displays all the DVDs in the alphabet sequence by title. In case they have the same title, the DVDs having the higher cost will be displayed first. o Sort by cost: the system the system displays all the DVDs in decreasing cost order. In case they have the same cost, the DVD
          """;
      String outputPath = "E:/Github Repo/MultiAgent_GenFix_Code/data/description.txt";
      analyst.analyzeAndSaveToJson(description, outputPath);
  }
}