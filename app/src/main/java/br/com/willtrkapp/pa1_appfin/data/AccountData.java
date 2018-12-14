package br.com.willtrkapp.pa1_appfin.data;
import br.com.willtrkapp.pa1_appfin.model.Account;
import br.com.willtrkapp.pa1_appfin.util.Utilitarios;
import br.com.willtrkapp.pa1_appfin.view.Balance;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class AccountData {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    public AccountData(Context context) {
        this.dbHelper=new SQLiteHelper(context);
    }

    public List<Account> allAccounts() {
        database = dbHelper.getReadableDatabase();
        List<Account> accounts = new ArrayList<>();
        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.TB_CONTA_KEY_ID, SQLiteHelper.TB_CONTA_KEY_DESCR, SQLiteHelper.TB_CONTA_KEY_SALDO_INI };
        cursor = database.query(SQLiteHelper.DB_TABLE_CONTA, cols, null , null,
                null, null, SQLiteHelper.TB_CONTA_KEY_DESCR);

        while (cursor.moveToNext()) {
            Account conta = new Account();
            conta.setId(cursor.getInt(0));
            conta.setDescription(cursor.getString(1));
            conta.setStartBalance(cursor.getFloat(2));
            accounts.add(conta);
        }
        cursor.close();

        database.close();
        return accounts;
    }

    public List<Balance> allAccountsPlusBalance() {
        database = dbHelper.getReadableDatabase();
        List<Balance> accounts = new ArrayList<>();
        Long dtTodayQuery = new Utilitarios().getCurrentUnixDate();

        String sql = "SELECT " + SQLiteHelper.DB_TABLE_CONTA + "." + SQLiteHelper.TB_CONTA_KEY_ID  +  ", " + SQLiteHelper.DB_TABLE_CONTA + "." + SQLiteHelper.TB_CONTA_KEY_DESCR  + ", " + SQLiteHelper.DB_TABLE_CONTA + "." + SQLiteHelper.TB_CONTA_KEY_SALDO_INI + ", " +
                SQLiteHelper.DB_TABLE_CONTA + "." + SQLiteHelper.TB_CONTA_KEY_SALDO_INI + " + IFNULL(SUM (" + SQLiteHelper.DB_TABLE_TRANSACAO  + "." + SQLiteHelper.TB_TRANSACAO_KEY_VALOR +
                "), 0) FROM " + SQLiteHelper.DB_TABLE_CONTA + " LEFT JOIN " +  SQLiteHelper.DB_TABLE_TRANSACAO + " ON " +  SQLiteHelper.DB_TABLE_TRANSACAO  + "." + SQLiteHelper.TB_TRANSACAO_KEY_ID_CONTA + " = " + SQLiteHelper.DB_TABLE_CONTA + "." + SQLiteHelper.TB_CONTA_KEY_ID +
                " AND " + SQLiteHelper.DB_TABLE_TRANSACAO + "." + SQLiteHelper.TB_TRANSACAO_KEY_DATA_LIB + " <= " + dtTodayQuery +
                "  GROUP BY " + SQLiteHelper.DB_TABLE_CONTA + "." + SQLiteHelper.TB_CONTA_KEY_ID + ";";

        Log.v("LOG_FIN_PA1", "SQL: " + sql);

        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Balance account = new Balance();
            account.setId(cursor.getLong(0));
            account.setDescription(cursor.getString(1));
            account.setStartBalance(cursor.getFloat(2));
            account.setSaldo(cursor.getFloat(3));
            accounts.add(account);
        }
        cursor.close();

        database.close();
        return accounts;

    }

    //Para controle do saldo inicial das contas e total das transações até a presente data
    public float getAccountBalance() {
        float startBalance = 0, transValue = 0;
        database = dbHelper.getReadableDatabase();

        String balanceAccount= "SELECT SUM(" + SQLiteHelper.TB_CONTA_KEY_SALDO_INI + ") FROM " + SQLiteHelper.DB_TABLE_CONTA + ";";
        Cursor cursorAccount = database.rawQuery(balanceAccount, null);
        if(cursorAccount.moveToFirst())
            startBalance = cursorAccount.getFloat(0);

        cursorAccount.close();

        Long dtTodayQuery = new Utilitarios().getCurrentUnixDate();
        String sqlTrans = "SELECT SUM(" + SQLiteHelper.TB_TRANSACAO_KEY_VALOR + ") FROM " + SQLiteHelper.DB_TABLE_TRANSACAO + " WHERE " + SQLiteHelper.DB_TABLE_TRANSACAO + "." + SQLiteHelper.TB_TRANSACAO_KEY_DATA_LIB + " <= " + dtTodayQuery + ";";
        Cursor cursorTrans = database.rawQuery(sqlTrans, null);
        if(cursorTrans.moveToFirst())
            transValue = cursorTrans.getFloat(0);

        cursorTrans.close();

        return startBalance + transValue;
    }

    public void salvaConta(Account c) {
        Log.v("LOG_FIN_PA1", "hit salvaConta");
        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.TB_CONTA_KEY_DESCR, c.getDescription());
        values.put(SQLiteHelper.TB_CONTA_KEY_SALDO_INI, c.getStartBalance());

        if (c.getId()>0) {
            database.update(SQLiteHelper.DB_TABLE_CONTA, values, SQLiteHelper.TB_CONTA_KEY_ID + "=" + c.getId(), null);
            Log.v("LOG_FIN_PA1", "fez update"); }
        else {
            database.insert(SQLiteHelper.DB_TABLE_CONTA, null, values);
            Log.v("LOG_FIN_PA1", "fez insert"); }
        database.close();
    }

    public void removeAccount(Account c) {
        database = dbHelper.getReadableDatabase();
        database.delete(SQLiteHelper.DB_TABLE_CONTA, SQLiteHelper.TB_CONTA_KEY_ID + "=" + c.getId(), null);
    }
}
