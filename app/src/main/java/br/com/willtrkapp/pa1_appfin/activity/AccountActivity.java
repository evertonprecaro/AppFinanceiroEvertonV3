package br.com.willtrkapp.pa1_appfin.activity;
import android.content.Intent;
import android.widget.EditText;
import br.com.willtrkapp.pa1_appfin.R;
import br.com.willtrkapp.pa1_appfin.data.AccountData;
import br.com.willtrkapp.pa1_appfin.model.Account;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class AccountActivity extends AppCompatActivity {
    private Account conta;
    private AccountData accountData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra("conta")) {
            this.conta = (Account) getIntent().getSerializableExtra("conta");
            EditText descrText = (EditText)findViewById(R.id.editTextDescricao);
            descrText.setText(conta.getDescription());

            EditText saldoIniText = (EditText)findViewById(R.id.editTextSaldoInicial);
            saldoIniText.setText(String.format("%.2f", conta.getStartBalance()));

            int pos =conta.getDescription().indexOf(" ");
            if (pos==-1)
                pos=conta.getDescription().length();
            setTitle(conta.getDescription().substring(0,pos)); }
        accountData = new AccountData(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);
        if (!getIntent().hasExtra("conta")){
            MenuItem item = menu.findItem(R.id.deleteMenu);
            item.setVisible(false); }
        return true;
    }

    private void delete() {
        accountData.removeAccount(conta);
        Intent resultIntent = new Intent();
        setResult(3,resultIntent);
        finish(); }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveMenu:
                save();
                return true;
            case R.id.deleteMenu:
                delete();
                return true;
            default:
                return super.onOptionsItemSelected(item); } }


    private void save() {
        String descr = ((EditText) findViewById(R.id.editTextDescricao)).getText().toString();
        float startBalance = Float.valueOf(((EditText) findViewById(R.id.editTextSaldoInicial)).getText().toString());
        if (conta==null)
            conta = new Account();
        conta.setDescription(descr);
        conta.setStartBalance(startBalance);
        accountData.salvaConta(conta);
        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish(); }

}
