package fb.controle.se.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "CONTROLE_SE"
        const val DATABASE_VERSION = 1
    }

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

}