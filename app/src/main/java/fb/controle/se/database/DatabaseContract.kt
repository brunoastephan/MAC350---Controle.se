package fb.controle.se.database

import android.provider.BaseColumns

object DatabaseContract {
    object TransactionsEntry : BaseColumns {
        const val TABLE_NAME = "TRANSACTIONS"
        const val COLUMN_DATE = "date"
        const val COLUMN_VALUE = "value"
        const val COLUMN_CATEGORY_ID = "category_id"
    }

    object CategoriesEntry : BaseColumns {
        const val TABLE_NAME = "CATEGORIES"
        const val COLUMN_NAME = "name"
        const val COLUMN_ICON = "icon"
    }

    object GoalsEntry : BaseColumns {
        const val TABLE_NAME = "GOALS"
        const val COLUMN_DUE_DATE = "due_date"
        const val COLUMN_EXPENSE_LIMIT = "expense_limit"
        const val COLUMN_CATEGORY_ID = "category_id"
    }

    private const val SQL_CREATE_TRANSACTIONS_ENTRIES =
        "CREATE TABLE ${TransactionsEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${TransactionsEntry.COLUMN_DATE} DATETIME NOT NULL," +
                "${TransactionsEntry.COLUMN_VALUE} FLOAT NOT NULL," +
                "${TransactionsEntry.COLUMN_CATEGORY_ID} INT NOT NULL," +
                "FOREIGN KEY (${TransactionsEntry.COLUMN_CATEGORY_ID}) REFERENCES ${CategoriesEntry.TABLE_NAME}(${BaseColumns._ID})" +
                ")"

    private const val SQL_CREATE_CATEGORIES_ENTRIES =
        "CREATE TABLE ${CategoriesEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${CategoriesEntry.COLUMN_NAME} TINYTEXT NOT NULL," +
                "${CategoriesEntry.COLUMN_ICON} NVARCHAR NOT NULL" +
                ")"

    private const val SQL_CREATE_GOALS_ENTRIES =
        "CREATE TABLE ${GoalsEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${GoalsEntry.COLUMN_DUE_DATE} DATETIME NOT NULL," +
                "${GoalsEntry.COLUMN_EXPENSE_LIMIT} FLOAT NOT NULL," +
                "${GoalsEntry.COLUMN_CATEGORY_ID} INT NOT NULL," +
                "FOREIGN KEY (${GoalsEntry.COLUMN_CATEGORY_ID}) REFERENCES ${CategoriesEntry.TABLE_NAME}(${BaseColumns._ID})" +
                ")"

    private const val SQL_DELETE_TRANSACTIONS_ENTRIES = "DROP TABLE IF EXISTS ${TransactionsEntry.TABLE_NAME}"
    private const val SQL_DELETE_CATEGORIES_ENTRIES = "DROP TABLE IF EXISTS ${CategoriesEntry.TABLE_NAME}"
    private const val SQL_DELETE_GOALS_ENTRIES = "DROP TABLE IF EXISTS ${GoalsEntry.TABLE_NAME}"

    val SQL_CREATE_ENTRIES_ARRAY = arrayOf(SQL_CREATE_TRANSACTIONS_ENTRIES, SQL_CREATE_CATEGORIES_ENTRIES, SQL_CREATE_GOALS_ENTRIES)
    val SQL_DELETE_ENTRIES_ARRAY = arrayOf(SQL_DELETE_TRANSACTIONS_ENTRIES, SQL_DELETE_CATEGORIES_ENTRIES, SQL_DELETE_GOALS_ENTRIES)

}