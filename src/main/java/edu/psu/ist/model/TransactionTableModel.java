package edu.psu.ist.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TransactionTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Type", "Date", "Description", "Amount"};
    private ArrayList<Transaction> transactionList;

    public TransactionTableModel(ArrayList<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public ArrayList<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(ArrayList<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public int getRowCount() {
        return this.transactionList.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return switch (col) {
            case 0 -> (Object) transactionList.get(row).getType();
            case 1 -> (Object) transactionList.get(row).getDate();
            case 2 -> (Object) transactionList.get(row).getDescriptionOfTransaction();
            case 3 -> (Object) transactionList.get(row).getAmount();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int col){
        return columnNames[col];
    }
}