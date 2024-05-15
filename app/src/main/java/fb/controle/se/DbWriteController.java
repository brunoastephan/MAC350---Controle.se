package fb.controle.se;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbWriteController{

    private SQLiteDatabase database;
    private final DbHelper dbHelper;

    public DbWriteController(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    public void open() { this.database = dbHelper.getWritableDatabase();}

    public void close() {this.database.close();}

    public void addTransaction(String date, float value, int category_id) {
        this.open();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("value", value);
        values.put("category_id", category_id);

        this.database.insert(dbHelper.getTransactionsTableName(), null, values);
    }

    public void addCategory(String name, String icon) {
        this.open();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("icon", icon);

        this.database.insert(dbHelper.getCategoriesTableName(), null, values);
    }

    public void addGoal(String due_date, float expense_limit, int category_id) {
        this.open();
        ContentValues values = new ContentValues();
        values.put("due_date", due_date);
        values.put("expense_limit", expense_limit);
        values.put("category_id", category_id);

        this.database.insert(dbHelper.getTransactionsTableName(), null, values);

    }

}
