package fb.controle.se

import android.provider.BaseColumns
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import fb.controle.se.database.DatabaseContract
import fb.controle.se.database.DbCategoryReader
import fb.controle.se.database.DbGoalReader
import fb.controle.se.database.DbHelper
import fb.controle.se.database.DbTransactionReader
import fb.controle.se.database.DbWriteController
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DbReadControllerTest {

    companion object {
        const val TEST_DATABASE_NAME = "TEST"
        const val TEST_DATABASE_VERSION = 1
        const val PRECISION = 1e-3F
    }

    private lateinit var dbWriteController: DbWriteController

    private lateinit var dbTransactionReader: DbTransactionReader
    private lateinit var dbCategoryReader: DbCategoryReader
    private lateinit var dbGoalReader: DbGoalReader
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


    private fun initializeDbWriter() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DbHelper(context, TEST_DATABASE_NAME, TEST_DATABASE_VERSION)
        dbWriteController = DbWriteController(context, dbHelper)
    }

    private fun initializeDbReaders() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DbHelper(context, TEST_DATABASE_NAME, TEST_DATABASE_VERSION)
        dbTransactionReader = DbTransactionReader(context, dbHelper)
        dbCategoryReader = DbCategoryReader(context, dbHelper)
        dbGoalReader = DbGoalReader(context, dbHelper)
    }

    private fun addDbSamples1() {
        dbWriteController.addCategory("cat1", "1")
        dbWriteController.addCategory("cat2", "2")

        dbWriteController.addTransaction(LocalDateTime.parse("2021-05-17 23:19:32", formatter), 12.5F, 1)
        dbWriteController.addTransaction(LocalDateTime.parse("2022-12-31 21:19:32", formatter), 33.5F, 2)
        dbWriteController.addTransaction(LocalDateTime.parse("2023-06-02 19:23:32", formatter), 45.23F, 1)
        dbWriteController.addTransaction(LocalDateTime.parse("2024-06-10 12:12:12", formatter), 420.69F, 1)
        dbWriteController.addTransaction(LocalDateTime.parse("2025-04-17 19:23:32", formatter), 45.23F, 2)
    }

    @Before
    fun setupTestEnv() {
        initializeDbWriter()
        initializeDbReaders()
    }

    @Test
    fun testReadTransactionsTotal() {
        // initialize execution with empty database
        assertEquals(0F, dbTransactionReader.readTransactionsTotal(), PRECISION)

        addDbSamples1()
        assertEquals(12.5F + 33.5F + 45.23F + 420.69F + 45.23F, dbTransactionReader.readTransactionsTotal(), PRECISION)
    }

    @Test
    fun testReadTransactionTotalFromIds() {
        // initialize execution with empty database
        var input: List<Int> = emptyList()
        var expected: Float = 0F
        var actual: Float = dbTransactionReader.readTransactionsTotalFromIds(emptyList())
        assertEquals(expected, actual, PRECISION)

        addDbSamples1()

        input = listOf(1)
        expected = 12.5F
        actual = dbTransactionReader.readTransactionsTotalFromIds(input)
        assertEquals(expected, actual, PRECISION)

        input = listOf(2, 3)
        expected = 33.5F + 45.23F
        actual = dbTransactionReader.readTransactionsTotalFromIds(input)
        assertEquals(expected, actual, PRECISION)
    }

    @Test
    fun testReadTransactionsInTimeInterval() {
        // initialize execution with empty database
        var beginTime: LocalDateTime = LocalDateTime.of(1980, 1, 1, 1, 1)
        var endTime: LocalDateTime = LocalDateTime.of(3000, 12, 30, 12, 59)
        var expected: MutableList<Int> = mutableListOf()
        var actual = dbTransactionReader.readTransactionsInTimeInterval(beginTime, endTime)
        assertEquals(expected, actual)

        addDbSamples1()

        beginTime = LocalDateTime.of(1980, 1, 1, 1, 1)
        endTime = LocalDateTime.of(3000, 12, 30, 12, 59)
        expected = mutableListOf(1, 2, 3, 4, 5)
        actual = dbTransactionReader.readTransactionsInTimeInterval(beginTime, endTime)
        assertEquals(expected, actual)

        beginTime = LocalDateTime.of(2022, 1, 1, 1, 1)
        endTime = LocalDateTime.of(2024, 12, 30, 12, 59)
        expected = mutableListOf(2, 3, 4)
        actual = dbTransactionReader.readTransactionsInTimeInterval(beginTime, endTime)
        assertEquals(expected, actual)
    }

    @Test
    fun testReadCategories() {
        var expected: List<Map<String, Any>> = listOf()
        var actual = dbCategoryReader.readCategories()
        assertEquals(expected, actual)

        addDbSamples1()

        expected = listOf(
            mapOf(BaseColumns._ID to 1, DatabaseContract.CategoriesEntry.COLUMN_NAME to "cat1", DatabaseContract.CategoriesEntry.COLUMN_ICON to "1"),
            mapOf(BaseColumns._ID to 2, DatabaseContract.CategoriesEntry.COLUMN_NAME to "cat2", DatabaseContract.CategoriesEntry.COLUMN_ICON to "2"),
        )
        actual = dbCategoryReader.readCategories()
        assertEquals(expected, actual)
    }

    fun testReadIconFromId() {
        var expected: String? = null
        var actual = dbCategoryReader.readIconFromId(1)
        assertEquals(expected, actual)

        addDbSamples1()

        expected = "1"
        actual = dbCategoryReader.readIconFromId(1)
        assertEquals(expected, actual)

        expected = "2"
        actual = dbCategoryReader.readIconFromId(2)
        assertEquals(expected, actual)
    }

    @After
    fun deleteDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(TEST_DATABASE_NAME)
    }
}