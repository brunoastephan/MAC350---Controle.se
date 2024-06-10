package fb.controle.se.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


open class DbHelper(private val context: Context, databaseName: String = "CONTROLE_SE", databaseVersion: Int = 1) : SQLiteOpenHelper(context, databaseName, null, databaseVersion) {

    override fun onCreate(db: SQLiteDatabase) {
        for (sqlCreateQuery in DatabaseContract.SQL_CREATE_ENTRIES_ARRAY) {
            db.execSQL(sqlCreateQuery)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        for (sqlDeleteQuery in DatabaseContract.SQL_DELETE_ENTRIES_ARRAY) {
            db.execSQL(sqlDeleteQuery)
        }
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun deleteDatabase() {
        context.deleteDatabase(databaseName)
    }
}