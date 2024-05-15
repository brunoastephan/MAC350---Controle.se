package fb.controle.se.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
class DbReadController(context: Context) {
    private var database : SQLiteDatabase

    private var dbHelper : DbHelper

    init {
        dbHelper = DbHelper(context)
        database = dbHelper.writableDatabase
    }
}