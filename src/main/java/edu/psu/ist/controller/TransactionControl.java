package edu.psu.ist.controller;

import edu.psu.ist.model.*;
import edu.psu.ist.view.TransactionUI;
import edu.psu.ist.view.TransactionListUI;
import edu.psu.ist.service.ChatService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.lang.Double.parseDouble;

public class TransactionControl implements ActionListener {
    private TransactionList theTransactionList;
    private TransactionTableModel theTransactionTableModel;
    private TransactionListUI theTransactionListUI;
    private TransactionUI theTransactionUI;
    private int currentRow;
    private ChatService chatService;

    public TransactionUI getTheTransactionUI() {
        return theTransactionUI;
    }

    public void setTheTransactionUI(TransactionUI theTransactionUI) {
        this.theTransactionUI = theTransactionUI;
    }

    public TransactionControl() {
        this.theTransactionList = new TransactionList();
        this.theTransactionTableModel = new TransactionTableModel(theTransactionList.getTransactions());
        this.theTransactionListUI = new TransactionListUI(this);
        this.chatService = new ChatService(); // Initialize chat service
        addALButtons();
        this.theTransactionListUI.setVisible(true);
    }

    public TransactionList getTheTransactionList() {
        return theTransactionList;
    }

    public void setTheTransactionList(TransactionList theTransactionList) {
        this.theTransactionList = theTransactionList;
    }

    public TransactionTableModel getTheTransactionTableModel() {
        return theTransactionTableModel;
    }

    public void setTheTransactionTableModel(TransactionTableModel theTransactionTableModel) {
        this.theTransactionTableModel = theTransactionTableModel;
    }

    public TransactionListUI getTheTransactionListUI() {
        return theTransactionListUI;
    }

    public void setTheTransactionListUI(TransactionListUI theTransactionListUI) {
        this.theTransactionListUI = theTransactionListUI;
    }

    public void getTransactionUI(int selectedRow){
        theTransactionUI = new TransactionUI(this, selectedRow);
        theTransactionListUI.setVisible(false);
        addDetailALButtons();
        theTransactionUI.setVisible(true);
    }

    public Transaction getTransaction(int selectedRow) {
        return this.theTransactionList.getTransactions().get(selectedRow);
    }

    public void addALButtons() {
        // List UI
        theTransactionListUI.newButton.addActionListener(this);
        theTransactionListUI.editButton.addActionListener(this);
        theTransactionListUI.deleteButton.addActionListener(this);
        theTransactionListUI.doneButton.addActionListener(this);
    }

    public void addDetailALButtons() {
        // Detail UI
        theTransactionUI.buttonUpdate.addActionListener(this);
        theTransactionUI.buttonDelete.addActionListener(this);
        theTransactionUI.buttonQuit.addActionListener(this);
    }

    // Add this method to handle chat messages
    public void handleChatMessage(String userMessage) {
        // Format transaction data for context
        String transactionData = chatService.formatTransactionData(theTransactionList.getTransactions());

        // Show user message immediately
        theTransactionListUI.addToChatLog("You: " + userMessage);

        // Process AI response in background thread to avoid freezing UI
        Thread aiThread = new Thread() {
            public void run() {
                String aiResponse = chatService.sendMessage(userMessage, transactionData);
                // Update UI on Event Dispatch Thread
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        theTransactionListUI.addToChatLog("AI Assistant: " + aiResponse);
                    }
                });
            }
        };
        aiThread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        // Add button
        if (obj==theTransactionListUI.newButton) {
            theTransactionList.getTransactions().add(new Transaction());

            // Save changes
            theTransactionList.writeTransactionListFile();

            // Update table and overview
            theTransactionTableModel.fireTableDataChanged();
            theTransactionListUI.updateOverview();
        }

        // Done Button
        if(obj==theTransactionListUI.doneButton) {
            // Save changes
            theTransactionList.writeTransactionListFile();
            System.exit(0);
        }

        if (theTransactionUI!=null) {

            // UPDATE button
            if (obj==theTransactionUI.buttonUpdate && !theTransactionList.getTransactions().isEmpty()) {
                this.currentRow = theTransactionUI.getCurrentSelectedRow();
                Transaction target = theTransactionList.getTransactions().get(currentRow);

                // Update Transaction Type
                target.setType(getTheTransactionUI().getTransactionType());

                // Update Transaction Date
                target.setDate(getTheTransactionUI().getTransactionDate());

                // Update Description of Transaction
                target.setDescriptionOfTransaction(getTheTransactionUI().getTransactionDescription());

                // Update Transaction Amount
                target.setAmount(parseDouble(getTheTransactionUI().getTransactionAmount()));

                // Exit after save
                theTransactionUI.dispose();
                theTransactionListUI.setVisible(true);

                // Save changes and update UI
                theTransactionList.writeTransactionListFile();
                theTransactionTableModel.fireTableDataChanged();
                theTransactionListUI.updateOverview();
            }

            // Delete Button
            if (obj==theTransactionUI.buttonDelete && !theTransactionList.getTransactions().isEmpty()) {
                this.currentRow = theTransactionUI.getCurrentSelectedRow();
                theTransactionList.getTransactions().remove(currentRow);
                theTransactionUI.dispose();
                theTransactionListUI.setVisible(true);

                // Save changes and update UI
                theTransactionList.writeTransactionListFile();
                theTransactionTableModel.fireTableDataChanged();
                theTransactionListUI.updateOverview();
            }

            // Quit Button
            if(obj==theTransactionUI.buttonQuit) {
                theTransactionUI.dispose();
                theTransactionListUI.setVisible(true);
                // Update overview when returning from detail view
                theTransactionListUI.updateOverview();
            }
        }
    }
}
