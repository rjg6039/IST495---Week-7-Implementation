package edu.psu.ist.view;

import edu.psu.ist.controller.TransactionControl;
import edu.psu.ist.model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TransactionListUI extends javax.swing.JFrame {
    private TransactionControl transactionControl;
    private ArrayList<Transaction> transactions;
    // Components
    private JPanel mainPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private JPanel chatPanel;
    private JPanel chatLogPanel;
    private JPanel chatInputPanel;
    private JPanel navigationPanel;
    private JPanel overviewPanel;
    private JTable transactionTable;
    public JButton doneButton;
    public JButton newButton;
    public JButton editButton;
    public JButton deleteButton;
    public JTextArea chatLogArea;
    public JTextField chatInputField;
    public JScrollPane tableScroller;
    public JScrollPane chatLogScroller;
    private JLabel totalExpensesLabel;
    private JLabel totalIncomesLabel;
    private JLabel runningBalanceLabel;
    private JButton dashboardButton;
    private JButton trendsButton;
    private JButton reportsButton;

    public TransactionListUI(TransactionControl transactionControl) {
        this.transactionControl = transactionControl;
        initComponents();
        this.transactions = transactionControl.getTheTransactionList().getTransactions();
        updateOverview();
    }

    public TransactionControl getTransactionControl() {
        return transactionControl;
    }

    public void setTransactionControl(TransactionControl transactionControl) {
        this.transactionControl = transactionControl;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public JTable getTransactionTable() {
        return transactionTable;
    }

    public void setTransactionTable(JTable transactionTable) {
        this.transactionTable = transactionTable;
    }

    private void initComponents() {
        // Create main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());

        // Create left panel for transaction list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Transaction List"));

        // Create navigation panel
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navigationPanel.setBorder(BorderFactory.createEtchedBorder());
        navigationPanel.setPreferredSize(new Dimension(700, 60));

        // Navigation buttons with larger font
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        dashboardButton = new JButton("Dashboard");
        newButton = new JButton("Transactions"); // Active view
        trendsButton = new JButton("Trends");
        reportsButton = new JButton("Reports");

        // Set button fonts and sizes
        JButton[] navButtons = {dashboardButton, newButton, trendsButton, reportsButton};
        for (JButton button : navButtons) {
            button.setFont(buttonFont);
            button.setPreferredSize(new Dimension(120, 35));
        }

        newButton.setBackground(Color.LIGHT_GRAY);
        newButton.setEnabled(false);

        // Add navigation buttons to panel in desired order
        navigationPanel.add(dashboardButton);    // 1st
        navigationPanel.add(newButton);          // 2nd - Transactions
        navigationPanel.add(trendsButton);       // 3rd
        navigationPanel.add(reportsButton);      // 4th

        // Create overview panel
        overviewPanel = new JPanel(new GridLayout(1, 3));
        overviewPanel.setBorder(BorderFactory.createEtchedBorder());
        overviewPanel.setPreferredSize(new Dimension(700, 70));

        // Overview labels with larger font
        Font overviewFont = new Font("Arial", Font.BOLD, 16);
        totalExpensesLabel = new JLabel("Total Expenses: $0.00");
        totalIncomesLabel = new JLabel("Total Incomes: $0.00");
        runningBalanceLabel = new JLabel("Running Balance: $0.00");

        totalExpensesLabel.setFont(overviewFont);
        totalIncomesLabel.setFont(overviewFont);
        runningBalanceLabel.setFont(overviewFont);

        // Add overview labels to panel
        overviewPanel.add(totalExpensesLabel);
        overviewPanel.add(totalIncomesLabel);
        overviewPanel.add(runningBalanceLabel);

        tablePanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        transactionTable = new JTable(transactionControl.getTheTransactionTableModel());
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 14));
        transactionTable.setRowHeight(25);
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(120);

        // Larger buttons
        Font largeButtonFont = new Font("Arial", Font.PLAIN, 14);
        doneButton = new JButton("Done");
        newButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        JButton[] actionButtons = {doneButton, newButton, editButton, deleteButton};
        for (JButton button : actionButtons) {
            button.setFont(largeButtonFont);
            button.setPreferredSize(new Dimension(100, 40));
        }

        newButton.addActionListener(new AddButtonListener());
        editButton.addActionListener(new EditButtonListener());
        deleteButton.addActionListener(new DeleteButtonListener());

        buttonPanel.add(newButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(doneButton);

        tableScroller = new JScrollPane(transactionTable);
        transactionTable.setFillsViewportHeight(true);
        tableScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tableScroller.setPreferredSize(new Dimension(700, 400));

        tablePanel.add(tableScroller);

        // Create a panel to hold navigation and overview panels vertically
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(navigationPanel, BorderLayout.NORTH);
        topPanel.add(overviewPanel, BorderLayout.SOUTH);
        topPanel.setPreferredSize(new Dimension(700, 130));

        // Add all components to left panel in proper order
        leftPanel.add(topPanel, BorderLayout.NORTH);
        leftPanel.add(tablePanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create right panel for chat - much wider now
        chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "AI Chat Log"));
        chatPanel.setPreferredSize(new Dimension(500, 600));

        chatLogPanel = new JPanel(new BorderLayout());
        chatLogArea = new JTextArea(15, 30);
        chatLogArea.setEditable(false);
        chatLogArea.setFont(new Font("Arial", Font.PLAIN, 14));
        chatLogArea.setLineWrap(true);
        chatLogArea.setWrapStyleWord(true);
        chatLogScroller = new JScrollPane(chatLogArea);
        chatLogScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatLogScroller.setPreferredSize(new Dimension(480, 400));
        chatLogPanel.add(chatLogScroller);

        chatInputPanel = new JPanel(new BorderLayout());
        chatInputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chatInputField = new JTextField();
        chatInputField.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.PLAIN, 14));
        sendButton.setPreferredSize(new Dimension(80, 35));

        // Add action listeners for chat
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendChatMessage();
            }
        });

        chatInputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendChatMessage();
            }
        });

        chatInputPanel.add(chatInputField, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(chatLogPanel, BorderLayout.CENTER);
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);

        // Add panels to main panel
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(chatPanel, BorderLayout.EAST);

        // Much larger window size (2x original)
        this.setSize(1200, 700);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Digital Checkbook Register");
    }

    // Method to update overview information
    public void updateOverview() {
        double totalExpenses = 0.0;
        double totalIncomes = 0.0;
        double balance = 0.0;

        for (Transaction transaction : transactions) {
            double amount = transaction.getAmount();
            if (amount < 0) {
                totalExpenses += Math.abs(amount);
            } else {
                totalIncomes += amount;
            }
            balance += amount;
        }

        totalExpensesLabel.setText("Total Expenses: $" + String.format("%.2f", totalExpenses));
        totalIncomesLabel.setText("Total Incomes: $" + String.format("%.2f", totalIncomes));
        runningBalanceLabel.setText("Running Balance: $" + String.format("%.2f", balance));
    }

    // Add this method for chat functionality
    public void addToChatLog(String message) {
        chatLogArea.append(message + "\n\n");
        chatLogArea.setCaretPosition(chatLogArea.getDocument().getLength());
    }

    // Add this method for sending chat messages
    private void sendChatMessage() {
        String message = chatInputField.getText().trim();
        if (!message.isEmpty()) {
            transactionControl.handleChatMessage(message);
            chatInputField.setText("");
        }
    }

    // Add helper methods for chat UI updates
    public String getChatLogContent() {
        return chatLogArea.getText();
    }

    public void setChatLogContent(String content) {
        chatLogArea.setText(content);
        chatLogArea.setCaretPosition(chatLogArea.getDocument().getLength());
    }

    private String replaceLast(String text, String search, String replacement) {
        int pos = text.lastIndexOf(search);
        if (pos == -1) {
            return text;
        }
        return text.substring(0, pos) + replacement + text.substring(pos + search.length());
    }

    public class AddButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create new transaction
            transactionControl.getTheTransactionList().getTransactions().add(new Transaction());
            // Refresh table
            transactionControl.getTheTransactionTableModel().fireTableDataChanged();
            // Update overview
            updateOverview();
            // Show new transaction details
            int newRow = transactionControl.getTheTransactionList().getTransactions().size() - 1;
            TransactionListUI.this.transactionControl.getTransactionUI(newRow);
        }
    }

    public class EditButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedTableRow = transactionTable.getSelectedRow();
            int selectedModelRow = transactionTable.convertRowIndexToModel(selectedTableRow);
            if(selectedModelRow < 0 || selectedModelRow > transactions.size() - 1)
                selectedModelRow = 0;
            TransactionListUI.this.transactionControl.getTransactionUI(selectedModelRow);
        }
    }

    public class DeleteButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedTableRow = transactionTable.getSelectedRow();
            int selectedModelRow = transactionTable.convertRowIndexToModel(selectedTableRow);
            if(selectedModelRow >= 0 && selectedModelRow < transactions.size()) {
                // Remove transaction
                transactionControl.getTheTransactionList().getTransactions().remove(selectedModelRow);
                // Refresh table
                transactionControl.getTheTransactionTableModel().fireTableDataChanged();
                // Update overview
                updateOverview();
            }
        }
    }
}
