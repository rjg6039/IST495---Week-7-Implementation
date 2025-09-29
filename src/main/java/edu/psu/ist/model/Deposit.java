package edu.psu.ist.model;

public class Deposit extends Transaction {
    public Deposit(String date, String descriptionOfTransaction, Double amount) {
        super(Type.DEPOSIT, date, descriptionOfTransaction, amount);
    }

    public Deposit(String date, Double amount) {
        super(Type.DEPOSIT, date, "Deposit", amount);
    }
}