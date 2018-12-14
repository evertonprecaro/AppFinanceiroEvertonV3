package br.com.willtrkapp.pa1_appfin.view;
import br.com.willtrkapp.pa1_appfin.model.Account;

public class Balance extends Account {
    private float saldo;

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }
}
