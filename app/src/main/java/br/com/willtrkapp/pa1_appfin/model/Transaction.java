package br.com.willtrkapp.pa1_appfin.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private int id;
    private long accountId;
    private long categoryId;
    private float valor;
    private String description;
    private int actionType; //1 para credito 2 = para debito
    private Date startDate; //Data da inserção
    private Date finalDate; //Data da liberação

    public Transaction() { }

    public Transaction(int id, int accountId, long categoryId, float valor, String description, int actionType, Date startDate, Date finalDate) {
        this.id = id;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.valor = valor;
        this.description = description;
        this.actionType = actionType;
        this.startDate = startDate;
        this.finalDate = finalDate;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public long getAccountId() {
        return accountId;
    }
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
    public long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getActionType() {
        return actionType;
    }
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getFinalDate() {
        return finalDate;
    }
    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return "Id: " + getId() + "\nIdConta " + getAccountId() + "\nIdCategoria " + getCategoryId() +
        "\nValor " + getValor() + "\nDescr " + getDescription() + "\nNatureza " + getActionType() + "\nDtIns " + format.format(getStartDate()) + "\nDtLib" + format.format(getFinalDate());
    }
}
