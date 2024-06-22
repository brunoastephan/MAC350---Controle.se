package fb.controle.se.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.core.graphics.rotationMatrix
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class DbReadController(context: Context, dbHelper: SQLiteOpenHelper = DbHelper(context)) {
    protected var database : SQLiteDatabase

    init {
        database = dbHelper.readableDatabase
    }
}

class DbTransactionReader(context: Context, dbHelper: SQLiteOpenHelper = DbHelper(context)) : DbReadController(context, dbHelper) {

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

    fun readTransactionsTotalFromIds(ids: List<Int>): Float {
        val selection = "${BaseColumns._ID} IN (${ids.joinToString()})"

        val cursor = database.query(
            DatabaseContract.TransactionsEntry.TABLE_NAME,
            arrayOf("SUM(${DatabaseContract.TransactionsEntry.COLUMN_VALUE})"),
            selection,
            null,
            null,
            null,
            null
        )

        var total : Float = 0F
        with (cursor) {
            if (moveToFirst()) total = getFloat(0)
        }
        cursor.close()

        return total
    }

    fun readTransactionsInTimeInterval(
        beginTime: LocalDateTime,
        endTime: LocalDateTime
    ): MutableList<Int> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val transactionsSumTimeIntervalSQL =
            "SELECT ${BaseColumns._ID} " +
                    "FROM ${DatabaseContract.TransactionsEntry.TABLE_NAME} " +
                    "WHERE ${DatabaseContract.TransactionsEntry.COLUMN_DATE} BETWEEN \'${
                        beginTime.format(
                            formatter
                        )
                    }\' AND \'${endTime.format(formatter)}\'"

        val cursor = database.rawQuery(transactionsSumTimeIntervalSQL, null)

        val transactions = mutableListOf<Int>()

        with(cursor) {
            while (moveToNext()) {
                val itemId = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                transactions.add(itemId)
            }
        }

        cursor.close()

        return transactions
    }

    fun readTransactionsInDay(): MutableList<Int> {
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

    fun readTransactionsInMonth(): MutableList<Int> {
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

    fun readTransactionsInYear(): MutableList<Int> {
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

        val selection = "${BaseColumns._ID} = ?"
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

    fun readTransactionsTotalFromCategoryId(categoryId: Int): Float {
        val selection = "${DatabaseContract.TransactionsEntry.COLUMN_CATEGORY_ID} IN (${categoryId})"

        val cursor = database.query(
            DatabaseContract.TransactionsEntry.TABLE_NAME,
            arrayOf("SUM(${DatabaseContract.TransactionsEntry.COLUMN_VALUE})"),
            selection,
            null,
            null,
            null,
            null
        )

        var total : Float = 0F
        with (cursor) {
            if (moveToFirst()) total = getFloat(0)
        }
        cursor.close()

        return total
    }

    fun readTransactionsFromCategoryIdInTimeInterval(
        categoryId: Int,
        beginTime: LocalDateTime,
        endTime: LocalDateTime
    ): MutableList<Int> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val transactionsSumTimeIntervalSQL =
            "SELECT ${BaseColumns._ID} " +
                    "FROM ${DatabaseContract.TransactionsEntry.TABLE_NAME} " +
                    "WHERE ${DatabaseContract.TransactionsEntry.COLUMN_DATE} BETWEEN \'${
                        beginTime.format(
                            formatter
                        )
                    }\' AND \'${endTime.format(formatter)}\' AND " +
                    "${DatabaseContract.TransactionsEntry.COLUMN_CATEGORY_ID} = $categoryId "

        val cursor = database.rawQuery(transactionsSumTimeIntervalSQL, null)

        val transactions = mutableListOf<Int>()

        with(cursor) {
            while (moveToNext()) {
                val itemId = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                transactions.add(itemId)
            }
        }

        cursor.close()

        return transactions
    }
}


class DbCategoryReader(context: Context, dbHelper: SQLiteOpenHelper = DbHelper(context)) : DbReadController(context, dbHelper) {
    private val projection = arrayOf(
        BaseColumns._ID,
        DatabaseContract.CategoriesEntry.COLUMN_NAME,
        DatabaseContract.CategoriesEntry.COLUMN_ICON
    )

    fun readCategories(): List<Map<String, Any>> {
        val cursor = database.rawQuery("SELECT * FROM ${DatabaseContract.CategoriesEntry.TABLE_NAME}", null)
        if (!cursor.moveToFirst()) {
            cursor.close()
            return listOf()
        }

        var categories = listOf<Map<String, Any>>()
        do {
            categories = categories + getCategoriesMap(cursor)
        } while (cursor.moveToNext())

        cursor.close()
        return categories
    }

    private fun getCategoriesMap(cursor: Cursor): Map<String, Any> {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(projection[0]))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]))
        val icon = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]))
        return mapOf(projection[0] to id, projection[1] to name, projection[2] to icon)
    }

    fun readIconFromId(id: Int): String? {
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = database.query(
            DatabaseContract.CategoriesEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var icon: String? = null
        with (cursor) {
            if (moveToFirst()) icon = getString(getColumnIndexOrThrow(projection[2]))
        }
        cursor.close()

        return icon
    }

}
class DbGoalReader(context: Context, dbHelper: SQLiteOpenHelper = DbHelper(context)) : DbReadController(context, dbHelper) {
    private val projection = arrayOf(
        BaseColumns._ID,
        DatabaseContract.GoalsEntry.COLUMN_DUE_DATE,
        DatabaseContract.GoalsEntry.COLUMN_EXPENSE_LIMIT,
        DatabaseContract.GoalsEntry.COLUMN_CATEGORY_ID
    )
}
