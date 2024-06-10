package fb.controle.se.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbWriteController(context : Context, dbHelper: SQLiteOpenHelper = DbHelper(context)) {
    private var database : SQLiteDatabase

    init {
        database = dbHelper.writableDatabase
    }

    fun addTransaction(date: String, value: Float, categoryId: Int) : Long {
        val valuesRow = ContentValues().apply {
            put(DatabaseContract.TransactionsEntry.COLUMN_DATE, date)
            put(DatabaseContract.TransactionsEntry.COLUMN_VALUE, value)
            put(DatabaseContract.TransactionsEntry.COLUMN_CATEGORY_ID, categoryId)
        }

        return database.insert(DatabaseContract.TransactionsEntry.TABLE_NAME, null, valuesRow)
    }

    fun addCategory(name: String, icon: String) : Long {
        val valuesRow = ContentValues().apply {
            put(DatabaseContract.CategoriesEntry.COLUMN_NAME, name)
            put(DatabaseContract.CategoriesEntry.COLUMN_ICON, icon)
        }

        return database.insert(DatabaseContract.CategoriesEntry.TABLE_NAME, null, valuesRow)
    }

    fun addGoal(dueDate: String, expenseLimit: Float, categoryId: Int) : Long {
        val valuesRow = ContentValues().apply {
            put(DatabaseContract.GoalsEntry.COLUMN_DUE_DATE, dueDate)
            put(DatabaseContract.GoalsEntry.COLUMN_EXPENSE_LIMIT, expenseLimit)
            put(DatabaseContract.GoalsEntry.COLUMN_CATEGORY_ID, categoryId)
        }

        return database.insert(DatabaseContract.GoalsEntry.TABLE_NAME, null, valuesRow)
    }
}