package edu.psu.ist.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction implements Serializable {
    public enum Type {CHECK, WITHDRAWAL, DEPOSIT}
    private Type type;
    private String date;
    private String descriptionOfTransaction;
    private Double amount;
    private Date dateObject;

    public Transaction(Type type, String date, String descriptionOfTransaction, Double amount) {
        this.type = type;
        this.date = date;
        this.descriptionOfTransaction = descriptionOfTransaction;
        this.amount = amount;
        parseDate();
    }

    public Transaction() {
        this.type = Type.DEPOSIT;
        this.date = "2024/10/20";
        this.descriptionOfTransaction = "New Empty Transaction";
        this.amount = 0.0;
        parseDate();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        parseDate();
    }

    public String getDescriptionOfTransaction() {
        return descriptionOfTransaction;
    }

    public void setDescriptionOfTransaction(String descriptionOfTransaction) {
        this.descriptionOfTransaction = descriptionOfTransaction;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDateObject() {
        return dateObject;
    }

    public void setDateObject(Date dateObject) {
        this.dateObject = dateObject;
        // Update the date string representation
        if (dateObject != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            this.date = sdf.format(dateObject);
        }
    }

    private void parseDate() {
        if (date != null && !date.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                this.dateObject = sdf.parse(date);
            } catch (ParseException e) {
                this.dateObject = new Date(); // Default to current date if parsing fails
            }
        } else {
            this.dateObject = new Date();
        }
    }

    @Override
    public String toString() {
        return type +
                " {date=" + date +
                ", descriptionOfTransaction='" + descriptionOfTransaction + '\'' +
                ", amount=" + amount +
                '}';
    }
}
