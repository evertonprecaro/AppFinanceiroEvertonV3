package br.com.willtrkapp.pa1_appfin.activity;
import br.com.willtrkapp.pa1_appfin.R;
import br.com.willtrkapp.pa1_appfin.data.CategoryData;
import br.com.willtrkapp.pa1_appfin.data.AccountData;
import br.com.willtrkapp.pa1_appfin.data.TransactionData;
import br.com.willtrkapp.pa1_appfin.model.Category;
import br.com.willtrkapp.pa1_appfin.model.Account;
import br.com.willtrkapp.pa1_appfin.model.Transaction;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

public class TransactionActivity extends AppCompatActivity {
    private int actionType; //1 para Credito 2 para Debito
    private TransactionData transactionData;
    private EditText editTextDtTrans;
    private CheckBox checkBoxRepeticoes;
    private NumberPicker numberPickerRepetitions;
    private List<Category> categorias;
    private List<Account> accounts;
    private Spinner categorySpinner, accountSpinner, recurringSpinner;
    private long idSelectCategory, idSelectAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacao);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTransacao);
        setSupportActionBar(toolbar);

       //Identificando tipo de operação da tela
        if(getIntent().getAction() == "Efetuar Crédito")
            actionType = 1;
        else
            actionType = 2;

        setTitle(getIntent().getAction());

        //Data para a transação
        editTextDtTrans = (EditText) findViewById(R.id.editTexDtTrans);

        recurringSpinner = (Spinner) findViewById(R.id.spinnerPeriodoRepeticoes);
        numberPickerRepetitions = (NumberPicker) findViewById(R.id.numberPickerRepeticoes);

        numberPickerRepetitions.setMaxValue(100);
        numberPickerRepetitions.setMinValue(2);
        numberPickerRepetitions.setWrapSelectorWheel(true);

        checkBoxRepeticoes = (CheckBox) findViewById(R.id.checkBoxRepeticoes);

        categorySpinner = findViewById(R.id.spinnerCategoriaTrans);
        accountSpinner = findViewById(R.id.spinnerContaTrans);

        setUpCalendar();
        setUpRecurring();
        setCategories();
        setUpAccountTrasaction();
        transactionData = new TransactionData(this);
    }

    private void setUpRecurring() {
        checkBoxRepeticoes.setOnClickListener(new View.OnClickListener()  {

              @Override
              public void onClick(View v) {
                  if(checkBoxRepeticoes.isChecked())
                      ((LinearLayout)findViewById(R.id.linearLayoutRepeticoes)).setVisibility(View.VISIBLE);
                  else
                      ((LinearLayout)findViewById(R.id.linearLayoutRepeticoes)).setVisibility(View.GONE);

              }
          }
        ); }

    private void setUpAccountTrasaction() {
        accounts = new AccountData(this).allAccounts();
        ArrayAdapter<Account> spinnerArrayAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_spinner_item, accounts); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                idSelectAccount = ((Account)parentView.getItemAtPosition(position)).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        accountSpinner.setAdapter(spinnerArrayAdapter); }

    private void save() {
        if(idSelectAccount != 0)
        {
            String strValor = ((EditText) findViewById(R.id.editTextValorTrans)).getText().toString();
            if(strValor != null && !strValor.isEmpty())
            {
                String descr = ((EditText) findViewById(R.id.editTextDescrTrans)).getText().toString();
                float valor = Float.valueOf(strValor);
                if(actionType != 1)
                    valor = valor * -1;

                Date dtToday = null, dtInicial = null;
                int count = 0;
                int numRepeticoes = 1;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                    dtToday = formatter.parse(formatter.format(new Date()));
                    dtInicial = formatter.parse(editTextDtTrans.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if(checkBoxRepeticoes.isChecked())
                    numRepeticoes = numberPickerRepetitions.getValue();

                Log.v("LOG_FIN_PA1", "Vai repetir: " + numRepeticoes);

                do {
                    Transaction transacao = new Transaction();
                    transacao.setActionType(actionType);
                    transacao.setDescription(descr);
                    transacao.setCategoryId(idSelectCategory);
                    transacao.setAccountId(idSelectAccount);
                    transacao.setStartDate(dtToday);
                    transacao.setFinalDate(getDtLib(dtInicial, count));
                    transacao.setValor(valor);
                    transactionData.saveTransaction(transacao);
                    Log.v("LOG_FIN_PA1", "Data Lib: " + formatter2.format(transacao.getFinalDate()));
                    count++;
                }while (count < numRepeticoes);

                Intent resultIntent = new Intent();
                resultIntent.setAction(actionType == 1 ? "Crédito realizado" : "Débito realizado");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
            else
                Toast.makeText(this, R.string.digite_o_valor_antes, Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, R.string.nenhuma_conta_selecionada_cadastre_uma_conta, Toast.LENGTH_SHORT).show();
    }

    private Date getDtLib(Date dtSelecionada, int iteracao) {
        Date recurringDate = dtSelecionada;
        Calendar c = Calendar.getInstance();
        c.setTime(recurringDate);

        switch (recurringSpinner.getSelectedItemPosition()) {
            case 0: //Diario
                c.add(Calendar.DATE, iteracao);
                break;

            case 1://Semanal
                c.add(Calendar.DATE, iteracao * 7);
                break;

            case 2://Mensal
                c.add(Calendar.MONTH, iteracao);
                break;

            case 3:
                c.add(Calendar.YEAR, iteracao);
                break;//Anual

            default:
                break; }

        recurringDate = c.getTime();


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            recurringDate = formatter.parse(formatter.format(recurringDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return recurringDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);
        if (!getIntent().hasExtra("transacao"))
        {
            MenuItem item = menu.findItem(R.id.deleteMenu);
            item.setVisible(false);
        }
        return true;
    }

    private void setUpCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DecimalFormat df = new DecimalFormat("00");
        editTextDtTrans.setText(df.format(dayOfMonth)  + "/" + df.format(month + 1 )+ "/" + year);
        editTextDtTrans.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(TransactionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                DecimalFormat df = new DecimalFormat("00");
                                editTextDtTrans.setText(df.format(day)  + "/" + df.format(month + 1 )+ "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }

    private void setCategories() {
        categorias = new CategoryData(this).buscaTodasCategorias();
        ArrayAdapter<Category> spinnerArrayAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item,  categorias); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                idSelectCategory = ((Category)parentView.getItemAtPosition(position)).getId(); }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorySpinner.setAdapter(spinnerArrayAdapter); }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveMenu:
                save();
                return true;
            case R.id.deleteMenu:
                /*delete();*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
