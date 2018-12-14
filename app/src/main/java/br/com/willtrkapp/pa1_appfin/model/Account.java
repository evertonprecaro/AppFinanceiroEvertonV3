package br.com.willtrkapp.pa1_appfin.model;
import java.io.Serializable;

public class Account implements Serializable {
    private long id;
    private String description;
    private float startBalance;

    public Account(){ }
    public Account(String description, float startBalance) {
        this.description = description;
        this.startBalance = startBalance;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public float getStartBalance() {
        return startBalance;
    }
    public void setStartBalance(float startBalance) {
        this.startBalance = startBalance;
    }

    @Override
    public  String toString()
    {
        return this.getDescription();
    }
}
