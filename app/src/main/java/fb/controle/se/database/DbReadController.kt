package fb.controle.se.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class DbReadController(context: Context) {
    protected var database : SQLiteDatabase

    private var dbHelper : DbHelper

    init {
        dbHelper = DbHelper(context)
        database = dbHelper.readableDatabase
    }
}

class DbTransactionReader(context: Context) : DbReadController(context) {

    private val projection = arrayOf(
        BaseColumns._ID,
        DatabaseContract.TransactionsEntry.COLUMN_DATE,
        DatabaseContract.TransactionsEntry.COLUMN_VALUE,
        DatabaseContract.TransactionsEntry.COLUMN_CATEGORY_ID
    )

    fun readTransactionsTotal() : Float {
        val totalColumn = "total"
        val transactionsSumSQL =
            "SELECT SUM(${DatabaseContract.TransactionsEntry.COLUMN_VALUE}) " +
                    "AS ${totalColumn} " +
                    "FROM ${DatabaseContract.TransactionsEntry.TABLE_NAME}"

        val cursor = database.rawQuery(transactionsSumSQL, null)

        if (!cursor.moveToFirst()) {
            cursor.close()
            return 0F
        }

        val total = cursor.getFloat(cursor.getColumnIndexOrThrow(totalColumn))

        cursor.close()

        return total
    }

    fun readTransactionsInTimeInterval(beginTime : LocalDateTime, endTime : LocalDateTime) :  MutableList<Int> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val transactionsSumTimeIntervalSQL =
            "SELECT ${BaseColumns._ID} " +
                    "FROM ${DatabaseContract.TransactionsEntry.TABLE_NAME} " +
                    "WHERE ${DatabaseContract.TransactionsEntry.COLUMN_DATE} BETWEEN \'${beginTime.format(formatter)}\' AND \'${endTime.format(formatter)}\'"

        val cursor = database.rawQuery(transactionsSumTimeIntervalSQL, null)

        val transactions = mutableListOf<Int>()

        with (cursor) {
            while (moveToNext()) {
                val itemId = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                transactions.add(itemId)
            }
        }

        cursor.close()

        return transactions
    }
    
    fun readTransactionsInDay() : MutableList<Int> {
        val endDateTime = LocalDateTime.now()
        val beginDateTime = LocalDateTime.of(
            endDateTime.year,
            endDateTime.month,
            endDateTime.dayOfMonth,
            0,
            0
        )

        return readTransactionsInTimeInterval(beginDateTime, endDateTime)
    }
    
    fun readTransactionsInMonth() : MutableList<Int> {
        val endDateTime = LocalDateTime.now()
        val beginDateTime = LocalDateTime.of(
            endDateTime.year,
            endDateTime.month,
            1,
            0,
            0
        )

        return readTransactionsInTimeInterval(beginDateTime, endDateTime)
    }
    
    fun readTransactionsInYear() : MutableList<Int> {
        val endDateTime = LocalDateTime.now()
        val beginDateTime = LocalDateTime.of(
            endDateTime.year,
            1,
            1,
            0,
            0
        )

        return readTransactionsInTimeInterval(beginDateTime, endDateTime)
    }

    fun readTransactionFromId(id: String) : List<Long> {

        val selection = "${DatabaseContract.TransactionsEntry.COLUMN_CATEGORY_ID} = ?"
        val selectionArgs = arrayOf(id)

        val cursor = database.query(
            DatabaseContract.TransactionsEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val itemIds = mutableListOf<Long>()
        with (cursor) {
            while (moveToNext()) {
                val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                itemIds.add(itemId)
            }
        }

        cursor.close()

        return itemIds

    }
}


class DbCategoryReader(context: Context) : DbReadController(context) {
    private val projection = arrayOf(
        BaseColumns._ID,
        DatabaseContract.CategoriesEntry.COLUMN_NAME,
        DatabaseContract.CategoriesEntry.COLUMN_ICON
    )
}
class DbGoalReader(context: Context) : DbReadController(context) {
    private val projection = arrayOf(
        BaseColumns._ID,
        DatabaseContract.GoalsEntry.COLUMN_DUE_DATE,
        DatabaseContract.GoalsEntry.COLUMN_EXPENSE_LIMIT,
        DatabaseContract.GoalsEntry.COLUMN_CATEGORY_ID
    )
}
