package edu.psu.ist.model;

public class Withdrawal extends Transaction{
    public Withdrawal(String date, Double amount) {
        // amount entered is negated since money is being removed (withdrawn)
        super(Type.WITHDRAWAL, date, "Withdrawal", (-1 * amount));
    }

    @Override
    public void setAmount(Double amount) {
        // amount entered is negated since money is being removed (withdrawn)
        super.setAmount(-1 * amount);
    }
}