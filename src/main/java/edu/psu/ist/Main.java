package edu.psu.ist;

import edu.psu.ist.controller.TransactionControl;
import edu.psu.ist.service.ChatService;

public class Main {
    public static void main(String[] args) {
        // Check if it's a test run
        if (args.length > 0 && args[0].equals("test")) {
            testChatService();
        } else {
            // Normal application run
            TransactionControl transactionControl = new TransactionControl();
        }
    }

    public static void testChatService() {
        try {
            System.out.println("Testing ChatService...");
            ChatService chatService = new ChatService();

            String testData = "Financial Summary:\n" +
                    "• Total Income: $1000.00 (2 deposits)\n" +
                    "• Total Expenses: $500.00 (3 withdrawals)\n" +
                    "• Current Balance: $500.00";

            String testMessage = "What's my spending pattern?";

            System.out.println("Sending test message to AI...");
            String response = chatService.sendMessage(testMessage, testData);
            System.out.println("AI Response: " + response);

        } catch (Exception e) {
            System.err.println("Test failed with error:");
            e.printStackTrace();
        }
    }
}
