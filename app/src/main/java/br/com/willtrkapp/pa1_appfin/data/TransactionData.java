package br.com.willtrkapp.pa1_appfin.data;
import br.com.willtrkapp.pa1_appfin.model.Transaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TransactionData {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    public TransactionData(Context context) {
        this.dbHelper=new SQLiteHelper(context);
    }

    public List<Transaction> allTransactions() {
        database=dbHelper.getReadableDatabase();
        List<Transaction> transacoes = new ArrayList<>();
        Cursor cursor;

        String[] cols = new String[] {SQLiteHelper.TB_TRANSACAO_KEY_ID, SQLiteHelper.TB_TRANSACAO_KEY_ID_CONTA, SQLiteHelper.TB_TRANSACAO_KEY_ID_CATEGORIA, SQLiteHelper.TB_TRANSACAO_KEY_VALOR,
           SQLiteHelper.TB_TRANSACAO_KEY_DESCR, SQLiteHelper.TB_TRANSACAO_KEY_NATUREZA, SQLiteHelper.TB_TRANSACAO_KEY_DATA_INS, SQLiteHelper.TB_TRANSACAO_KEY_DATA_LIB };
        cursor = database.query(SQLiteHelper.DB_TABLE_TRANSACAO, cols, null , null,
                null, null, SQLiteHelper.TB_TRANSACAO_KEY_DESCR);

        while (cursor.moveToNext()) {
            Transaction trans = new Transaction();
            trans.setId(cursor.getInt(0));
            trans.setAccountId(cursor.getInt(1));
            trans.setCategoryId(cursor.getInt(2));
            trans.setValor(cursor.getFloat(3));
            trans.setDescription(cursor.getString(4));
            trans.setActionType(cursor.getInt(5));
            trans.setStartDate(new Date(cursor.getLong(6) * 1000));
            trans.setFinalDate(new Date(cursor.getLong(7) * 1000));
            transacoes.add(trans); }
        cursor.close();
        database.close();
        return transacoes;
    }

    public void saveTransaction(Transaction trans) {
        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.TB_TRANSACAO_KEY_ID_CONTA, trans.getAccountId());
        values.put(SQLiteHelper.TB_TRANSACAO_KEY_ID_CATEGORIA, trans.getCategoryId());
        values.put(SQLiteHelper.TB_TRANSACAO_KEY_VALOR, trans.getValor());
        values.put(SQLiteHelper.TB_TRANSACAO_KEY_DESCR, trans.getDescription());
        values.put(SQLiteHelper.TB_TRANSACAO_KEY_NATUREZA, trans.getActionType());
        values.put(SQLiteHelper.TB_TRANSACAO_KEY_DATA_INS, trans.getStartDate().getTime() / 1000);
        values.put(SQLiteHelper.TB_TRANSACAO_KEY_DATA_LIB, trans.getFinalDate().getTime() / 1000);

        if (trans.getId()>0)
            database.update(SQLiteHelper.DB_TABLE_TRANSACAO, values, SQLiteHelper.TB_TRANSACAO_KEY_ID + "="
                    + trans.getId(), null);
        else
            database.insert(SQLiteHelper.DB_TABLE_TRANSACAO, null, values);

        database.close();

    }

    public void removeTransaction(Transaction trans) {
        dbHelper.getReadableDatabase().delete(SQLiteHelper.DB_TABLE_TRANSACAO, SQLiteHelper.TB_TRANSACAO_KEY_ID + "=" + trans.getId(), null);
    }
}
