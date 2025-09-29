package edu.psu.ist.view;

import edu.psu.ist.controller.TransactionControl;
import edu.psu.ist.model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionUI extends javax.swing.JFrame{
    private TransactionControl transactionControl;
    private int currentSelectedRow;
    private Transaction currentTransaction;

    // Components
    private JPanel mainPanel;
    private JPanel detailPanel;
    private JPanel buttonPanel;
    private JLabel typeLabel;
    private JLabel dateLabel;
    private JLabel descriptionLabel;
    private JLabel amountLabel;
    private JComboBox<Transaction.Type> transactionType;
    private JPanel datePanel;
    private JFormattedTextField dateField;
    private JTextField descriptionField;
    private JTextField amountField;
    private JButton calendarButton;

    public JButton buttonDelete;
    public JButton buttonQuit;
    public JButton buttonUpdate;

    public TransactionUI(TransactionControl newTransactionControl, int selectedRow){
        this.transactionControl = newTransactionControl;
        currentSelectedRow = selectedRow;
        currentTransaction = transactionControl.getTransaction(selectedRow); //calls the getTransaction method
        initComponents();
        parseTransaction();  //new method that must be added
    }

    public TransactionControl getTransactionControl() {
        return transactionControl;
    }
    public void setTransactionControl(TransactionControl transactionControl) {
        this.transactionControl = transactionControl;
    }
    public int getCurrentSelectedRow() {
        return currentSelectedRow;
    }
    public void setCurrentSelectedRow(int currentSelectedRow) {
        this.currentSelectedRow = currentSelectedRow;
    }
    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }
    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }
    public Transaction.Type getTransactionType() {
        return (Transaction.Type) transactionType.getSelectedItem();
    }
    public String getTransactionDate() {
        return dateField.getText();
    }
    public String getTransactionDescription() {
        return descriptionField.getText();
    }
    public String getTransactionAmount() {
        return amountField.getText();
    }

    private void initComponents() {
        // Create bento box panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Transaction Details"));

        detailPanel = new JPanel(new GridLayout(4,2));
        buttonPanel = new JPanel(new GridLayout(1,3));

        // Labels
        typeLabel = new JLabel("Transaction Type: ");
        dateLabel = new JLabel("Transaction Date: ");
        descriptionLabel = new JLabel("Transaction Description: ");
        amountLabel = new JLabel("Transaction Amount: ");

        // Fields
        transactionType = new JComboBox<>(Transaction.Type.values());
        datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dateField = new JFormattedTextField();
        dateField.setColumns(15);
        descriptionField = new JTextField();
        amountField = new JTextField();

        // Calendar button
        calendarButton = new JButton("📅");
        calendarButton.setPreferredSize(new Dimension(30, 25));
        calendarButton.addActionListener(new CalendarButtonListener());

        // Add components to date panel
        datePanel.add(dateField);
        datePanel.add(calendarButton);

        // Buttons
        buttonUpdate = new JButton("Save");
        buttonDelete = new JButton("Delete");
        buttonQuit = new JButton("Done");
        buttonPanel.add(buttonUpdate);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(buttonQuit);

        // Details
        detailPanel.add(typeLabel);
        detailPanel.add(transactionType);
        detailPanel.add(dateLabel);
        detailPanel.add(datePanel);
        detailPanel.add(descriptionLabel);
        detailPanel.add(descriptionField);
        detailPanel.add(amountLabel);
        detailPanel.add(amountField);

        // Structure Default Window
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);

        // Add components to main panel
        mainPanel.add(detailPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Transaction Details");
    }

    public void parseTransaction(){
        transactionType.setSelectedItem(currentTransaction.getType());
        dateField.setText(currentTransaction.getDate());
        descriptionField.setText(currentTransaction.getDescriptionOfTransaction());
        amountField.setText("" + currentTransaction.getAmount());
    }

    public class CalendarButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a simple date picker dialog
            JDialog datePickerDialog = new JDialog(TransactionUI.this, "Select Date", true);
            datePickerDialog.setLayout(new BorderLayout());

            // Create date picker components
            JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(2024, 1900, 2100, 1));
            JSpinner monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
            JSpinner daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));

            JPanel datePanel = new JPanel(new FlowLayout());
            datePanel.add(new JLabel("Year:"));
            datePanel.add(yearSpinner);
            datePanel.add(new JLabel("Month:"));
            datePanel.add(monthSpinner);
            datePanel.add(new JLabel("Day:"));
            datePanel.add(daySpinner);

            JButton selectButton = new JButton("Select Date");
            selectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int year = (Integer) yearSpinner.getValue();
                    int month = (Integer) monthSpinner.getValue();
                    int day = (Integer) daySpinner.getValue();

                    // Format the date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    String selectedDate = String.format("%04d/%02d/%02d", year, month, day);
                    dateField.setText(selectedDate);
                    datePickerDialog.dispose();
                }
            });

            datePickerDialog.add(datePanel, BorderLayout.CENTER);
            datePickerDialog.add(selectButton, BorderLayout.SOUTH);

            datePickerDialog.setSize(300, 150);
            datePickerDialog.setLocationRelativeTo(TransactionUI.this);
            datePickerDialog.setVisible(true);
        }
    }
}
