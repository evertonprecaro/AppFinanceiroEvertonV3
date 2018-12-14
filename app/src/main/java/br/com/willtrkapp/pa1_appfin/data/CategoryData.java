package br.com.willtrkapp.pa1_appfin.data;
import java.util.ArrayList;
import br.com.willtrkapp.pa1_appfin.model.Category;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;

public class CategoryData {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public CategoryData(Context context) {
        this.dbHelper=new SQLiteHelper(context);
    }

    public List<Category> buscaTodasCategorias() {
        database=dbHelper.getReadableDatabase();
        List<Category> categorias = new ArrayList<>();
        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.TB_CATEGORIA_KEY_ID, SQLiteHelper.TB_CATEGORIA_KEY_DESCR };
        cursor = database.query(SQLiteHelper.DB_TABLE_CATEGORIA, cols, null , null,
                null, null, SQLiteHelper.TB_CATEGORIA_KEY_DESCR);

        while (cursor.moveToNext()) {
            Category categ = new Category();
            categ.setId(cursor.getInt(0));
            categ.setDescription(cursor.getString(1));
            categorias.add(categ); }
        cursor.close();
        database.close();
        return categorias;
    }
}
