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

    public void addElement(String mail, String phone) {
        this.open();
        ContentValues values = new ContentValues();
        values.put("mail", mail);
        values.put("phone", phone);

        this.database.insert(dbHelper.getTableName(), null, values);
    }

}
