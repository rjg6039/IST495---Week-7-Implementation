package edu.psu.ist.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.SocketTimeoutException;
import java.util.List;

public class ChatService {
    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String API_KEY = "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";
    private Gson gson;

    public ChatService() {
        this.gson = new Gson();
    }

    public String sendMessage(String userMessage, String transactionData) {
        try {
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", "llama-3.1-8b-instant"); // Updated to production model
            requestBody.addProperty("temperature", 0.7);
            requestBody.addProperty("max_tokens", 1000);

            JsonArray messages = new JsonArray();

            JsonObject systemMessage = new JsonObject();
            systemMessage.addProperty("role", "system");
            systemMessage.addProperty("content",
                    "You are a helpful financial assistant. You help users understand their spending patterns, " +
                            "provide budgeting advice, and answer general personal finance questions. " +
                            "When analyzing user data, be specific about amounts and dates. Keep responses concise but helpful.");
            messages.add(systemMessage);

            JsonObject userMessageObj = new JsonObject();
            userMessageObj.addProperty("role", "user");
            userMessageObj.addProperty("content",
                    "My financial data:\n" + transactionData + "\n\nMy question: " + userMessage);
            messages.add(userMessageObj);

            requestBody.add("messages", messages);

            System.out.println("Sending request to Groq API...");
            String response = sendPostRequest(GROQ_API_URL, requestBody.toString());
            System.out.println("Received response from Groq API");

            JsonObject responseJson = gson.fromJson(response, JsonObject.class);

            // Check for API errors
            if (responseJson.has("error")) {
                JsonObject error = responseJson.getAsJsonObject("error");
                String errorMessage = error.has("message") ? error.get("message").getAsString() : "Unknown API error";
                return "API Error: " + errorMessage;
            }

            if (responseJson.has("choices") && responseJson.getAsJsonArray("choices").size() > 0) {
                return responseJson.getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .get("message").getAsJsonObject()
                        .get("content").getAsString();
            } else {
                return "I'm having trouble processing your request. Please try again. Response: " + response;
            }

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return "Sorry, I received an unexpected response format from the AI service. Error: " + e.getMessage();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "Sorry, the AI service is taking too long to respond. Please try again.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Sorry, I'm having trouble connecting to the AI assistant. Please check your internet connection. Error: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I'm having trouble processing your request. Error: " + e.getMessage();
        }
    }

    private String sendPostRequest(String urlString, String jsonInputString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + API_KEY);
        con.setDoOutput(true);
        con.setConnectTimeout(30000); // 30 second timeout
        con.setReadTimeout(30000);

        System.out.println("Request body: " + jsonInputString);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        System.out.println("Response code: " + responseCode);

        InputStream inputStream;
        String responseMessage = con.getResponseMessage();
        System.out.println("Response message: " + responseMessage);

        if (responseCode >= 200 && responseCode < 300) {
            inputStream = con.getInputStream();
        } else {
            inputStream = con.getErrorStream();
            // If there's no error stream, create a message
            if (inputStream == null) {
                return "{\"error\": {\"message\": \"HTTP " + responseCode + " " + responseMessage + "\"}}";
            }
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            String responseString = response.toString();
            System.out.println("Response body: " + responseString);
            return responseString;
        }
    }

    public String formatTransactionData(List<edu.psu.ist.model.Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return "No transaction data available.";
        }

        double totalIncome = 0.0;
        double totalExpenses = 0.0;
        int depositCount = 0;
        int withdrawalCount = 0;

        // Calculate totals
        for (edu.psu.ist.model.Transaction t : transactions) {
            if (t.getAmount() > 0) {
                totalIncome += t.getAmount();
                depositCount++;
            } else {
                totalExpenses += Math.abs(t.getAmount());
                withdrawalCount++;
            }
        }

        double currentBalance = totalIncome - totalExpenses;

        // Get recent transactions (last 5)
        StringBuilder recentTransactions = new StringBuilder();
        int startIdx = Math.max(0, transactions.size() - 5);
        for (int i = startIdx; i < transactions.size(); i++) {
            edu.psu.ist.model.Transaction t = transactions.get(i);
            String amountStr = String.format("$%.2f", Math.abs(t.getAmount()));
            recentTransactions.append(String.format("• %s: %s - %s: %s\n",
                    t.getDate(), t.getType(), t.getDescriptionOfTransaction(), amountStr));
        }

        return String.format(
                "Financial Summary:\n" +
                        "• Total Income: $%.2f (%d deposits)\n" +
                        "• Total Expenses: $%.2f (%d withdrawals)\n" +
                        "• Current Balance: $%.2f\n\n" +
                        "Recent Transactions:\n%s",
                totalIncome, depositCount, totalExpenses, withdrawalCount,
                currentBalance, recentTransactions.toString()
        );
    }
}
