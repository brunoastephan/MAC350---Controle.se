package fb.controle.se;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CONTROLE_SE";
    private static final int VERSION = 1;
    private static final String TRANSACTIONS_TABLE = "TRANSACTIONS";
    private static final String CATEGORIES_TABLE = "CATEGORIES";
    private static final String GOALS_TABLE = "GOALS";

    private static final String[] TABLES = new String[]{TRANSACTIONS_TABLE, CATEGORIES_TABLE, GOALS_TABLE};


    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // CREATE TRANSACTIONS_TABLE
        db.execSQL("CREATE TABLE " + TRANSACTIONS_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date DATETIME NOT NULL," +
                "value FLOAT NOT NULL," +
                "category_id INT NOT NULL, " +
                "FOREIGN KEY (category_id) REFERENCES CATEGORIES(id)" +
                ")");

        // CREATE CATEGORIES_TABLE
        db.execSQL("CREATE TABLE " + CATEGORIES_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TINYTEXT NOT NULL," +
                "icon NVARCHAR NOT NULL" +
                ")"
        );

        // CREATE GOALS_TABLE
        db.execSQL("CREATE TABLE " + GOALS_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "due_date DATETIME NOT NULL," +
                "expense_limit FLOAT NOT NULL," +
                "category_id INT NOT NULL, " +
                "FOREIGN KEY (category_id) REFERENCES CATEGORIES(id)" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String table: TABLES)
            db.execSQL("DROP TABLE IF EXISTS " + table);
        onCreate(db);
    }

    public String getTransactionsTableName() {return TRANSACTIONS_TABLE;}
    public String getCategoriesTableName() {return CATEGORIES_TABLE;}
    public String getGoalsTableName() {return GOALS_TABLE;}

}
