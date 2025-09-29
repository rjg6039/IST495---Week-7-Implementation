package edu.psu.ist.model;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Ryan Griesbach
 */
public class TransactionList {

    private ArrayList<Transaction> transactions = new ArrayList<>();
    private String listOfTransactionsFileName = "listOfTransactions.ser";

    // Instructions Requirement 2: Controller class creates the ArrayList of the model objects
    public TransactionList(ArrayList<Transaction> transactions) {

        // ArrayList created in Controller is passed to TransactionList
        this.transactions = transactions;
    }
    public TransactionList() {
        this.readTransactionListFile();
        if(transactions.isEmpty() || transactions == null) {
            this.createManualTransactionList();
            this.writeTransactionListFile();
            this.readTransactionListFile();
        }
        this.printTransactionList();
    }

    public ArrayList<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    //TODO Add methods for reading and writing data (see M07-L01 from last week).

    public void readTransactionListFile() {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(listOfTransactionsFileName);
            in = new ObjectInputStream(fis);
            transactions = (ArrayList) in.readObject();
            in.close();
            if (!transactions.isEmpty()) {
                System.out.println("There are transactions in the transaction list");
            }

        } catch (FileNotFoundException fne) {
            System.out.println("File was not found, a new one will be created");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    public void writeTransactionListFile() {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(listOfTransactionsFileName);
            out = new ObjectOutputStream(fos);
            out.writeObject(transactions);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createManualTransactionList() {
        // Clear any existing transactions
        transactions.clear();

        // Add 5 sample transactions based on typical checkbook entries
        transactions.add(new Deposit("2024/11/08", "Salary Deposit", 2500.00));
        transactions.add(new Withdrawal("2024/11/10", 75.50));
        transactions.add(new Withdrawal("2024/11/12", 45.00));
        transactions.add(new Deposit("2024/11/15", "Freelance Payment", 800.00));
        transactions.add(new Withdrawal("2024/11/18", 120.75));

        System.out.println("Manual TransactionList created with 5 sample transactions");
        System.out.println("The TransactionList is: " + transactions);
    }

    public void printTransactionList() {
        System.out.println("The TransactionList has these transactions:");
        for (int i = 0; i < transactions.size(); i++) {
            Transaction currentTransaction = (Transaction) transactions.get(i);
            // Transaction class has an overridden toString that will provide sufficient detail
            System.out.println(currentTransaction);
        }
    }
}
