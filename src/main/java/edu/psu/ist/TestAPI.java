package edu.psu.ist;

import edu.psu.ist.service.ChatService;

public class TestAPI {
    public static void main(String[] args) {
        ChatService chatService = new ChatService();
        String testData = "Test financial data";
        String testMessage = "Hello, this is a test message";

        System.out.println("Testing Groq API connection...");
        String response = chatService.sendMessage(testMessage, testData);
        System.out.println("Response: " + response);
    }
}
