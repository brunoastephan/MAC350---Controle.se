package fb.controle.se;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbReadController {
    private SQLiteDatabase database;
    private final DbHelper dbHelper;

    public DbReadController(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    public void open() { this.database = dbHelper.getReadableDatabase();}

    public void close() {this.database.close();}
}
