package fb.controle.se.utils

import android.content.Context
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import fb.controle.se.R
import fb.controle.se.database.DbCategoryReader
import fb.controle.se.database.DbTransactionReader
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters.firstDayOfMonth
import java.time.temporal.TemporalAdjusters.firstDayOfYear
import java.time.temporal.TemporalAdjusters.lastDayOfMonth
import java.time.temporal.TemporalAdjusters.lastDayOfYear
import kotlin.random.Random

fun getDayRange(currTime: LocalDateTime, day: Int): Pair<LocalDateTime, LocalDateTime> {
    val beginTime = currTime.withDayOfMonth(day).withHour(0).withMinute(0).withSecond(0)
    val endTime = currTime.withDayOfMonth(day).withHour(23).withMinute(59).withSecond(59)
    return Pair(beginTime, endTime)
}

fun getMonthRange(currTime: LocalDateTime, month: Int): Pair<LocalDateTime, LocalDateTime> {
    val beginTime = currTime.withMonth(month).with(firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0)
    val endTime = currTime.withMonth(month).with(lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59)
    return Pair(beginTime, endTime)
}

fun getYearRange(currTime: LocalDateTime, year: Int): Pair<LocalDateTime, LocalDateTime> {
    val beginTime = currTime.withYear(year).with(firstDayOfYear()).withHour(0).withMinute(0).withSecond(0)
    val endTime = currTime.withYear(year).with(lastDayOfYear()).withHour(23).withMinute(59).withSecond(59)
    return Pair(beginTime, endTime)
}

class DynamicTimeBarChart(
    private val context: Context,
    private val barChart: BarChart,
    private var viewState: TransactionViewState
) {

    private var dbTransactionReader: DbTransactionReader = DbTransactionReader(context)
    private val colorTemplate = ColorTemplate.COLORFUL_COLORS.toList()

    init {
        setupBarChart()
    }

    fun updateState(newViewState: TransactionViewState) {
        viewState = newViewState
        setupBarChart()
    }

    fun setupBarChart() {
        when (viewState) {
            TransactionViewState.DAY -> setupBarChartDay()
            TransactionViewState.MONTH -> setupBarChartMonth()
            TransactionViewState.YEAR -> setupBarChartYear()
        }
    }

    private fun setupBarChartDay() {
        val currTime = LocalDateTime.now()
        val daysInMonth = currTime.with(lastDayOfMonth()).dayOfMonth
        val days = (1..daysInMonth).toList().map { it.toString() }  // Get days of month as strings

        val dayRanges = ArrayList<Pair<LocalDateTime, LocalDateTime>>()
        for (day in 1..daysInMonth) dayRanges.add(getDayRange(currTime, day))

        val dayTransactionTotals = ArrayList<Float>()
        for (dayRange in dayRanges) {
            val transactionIds = dbTransactionReader.readTransactionsInTimeInterval(dayRange.first, dayRange.second)
            dayTransactionTotals.add(dbTransactionReader.readTransactionsTotalFromIds(transactionIds))
        }

        val barChartEntries = ArrayList<BarEntry>()
        for ((day, dayTransactionTotal) in dayTransactionTotals.withIndex()) {
            if (dayTransactionTotal > 0)
                barChartEntries.add(BarEntry(day.toFloat(), dayTransactionTotal))
        }

        val barDataSet = BarDataSet(barChartEntries, null)
        barDataSet.colors = colorTemplate

        val barData = BarData(barDataSet)
        configBarChart(barData, context.getString(R.string.bar_chart_description_day), days)
    }

    private fun setupBarChartMonth() {
        val months = listOf(
            context.getString(R.string.month_january),
            context.getString(R.string.month_february),
            context.getString(R.string.month_march),
            context.getString(R.string.month_april),
            context.getString(R.string.month_may),
            context.getString(R.string.month_june),
            context.getString(R.string.month_july),
            context.getString(R.string.month_august),
            context.getString(R.string.month_september),
            context.getString(R.string.month_october),
            context.getString(R.string.month_november),
            context.getString(R.string.month_december)
        )

        val monthRanges = ArrayList<Pair<LocalDateTime, LocalDateTime>>()
        val currTime = LocalDateTime.now()
        for (month in 1..12) monthRanges.add(getMonthRange(currTime, month))

        val monthTransactionTotals = ArrayList<Float>()
        for (monthRange in monthRanges) {
            val transactionIds = dbTransactionReader.readTransactionsInTimeInterval(monthRange.first, monthRange.second)
            monthTransactionTotals.add(dbTransactionReader.readTransactionsTotalFromIds(transactionIds))
        }

        val barChartEntries = ArrayList<BarEntry>()
        for ((month, monthTransactionTotal) in monthTransactionTotals.withIndex()) {
            if (monthTransactionTotal > 0)
                barChartEntries.add(BarEntry(month.toFloat(), monthTransactionTotal))
        }

        val barDataSet = BarDataSet(barChartEntries, null)
        barDataSet.colors = colorTemplate

        val barData = BarData(barDataSet)
        configBarChart(barData, context.getString(R.string.bar_chart_description_month), months)
    }

    private fun setupBarChartYear() {
        val currTime = LocalDateTime.now()
        val currentYear = currTime.year
        val years = (-10..10).map { (currentYear + it).toString() }

        val yearRanges = ArrayList<Pair<LocalDateTime, LocalDateTime>>()
        for (year in years) yearRanges.add(getYearRange(currTime, year.toInt()))

        val yearTransactionTotals = ArrayList<Float>()
        for (yearRange in yearRanges) {
            val transactionIds = dbTransactionReader.readTransactionsInTimeInterval(yearRange.first, yearRange.second)
            yearTransactionTotals.add(dbTransactionReader.readTransactionsTotalFromIds(transactionIds))
        }

        val barChartEntries = ArrayList<BarEntry>()
        for ((year, yearTransactionTotal) in yearTransactionTotals.withIndex()) {
            if (yearTransactionTotal > 0)
                barChartEntries.add(BarEntry(year.toFloat(), yearTransactionTotal))
        }

        val barDataSet = BarDataSet(barChartEntries, null)
        barDataSet.colors = colorTemplate

        val barData = BarData(barDataSet)
        configBarChart(barData, context.getString(R.string.bar_chart_description_year), years)
    }

    private fun configBarChart(barData: BarData, textDescription: String,indexValueFormatter: Collection<String>) {
        barChart.setFitBars(false)
        barChart.setDrawBarShadow(false)
        barChart.setDrawGridBackground(false)
        barChart.data = barData
        barChart.description.text = textDescription
        barChart.setVisibleXRangeMaximum(3F)

        val xAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(true)
        xAxis.setDrawAxisLine(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1F
        xAxis.valueFormatter = IndexAxisValueFormatter(indexValueFormatter)

        val yAxisLeft = barChart.axisLeft
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.setDrawAxisLine(false)
        yAxisLeft.setEnabled(false)

        barChart.axisRight.setEnabled(false)

        barChart.animateY(1000)
    }
}

fun plotPieChart(viewState: TransactionViewState) {

}

fun plotPieChartDay() {

}

fun plotPieChartMonth() {

}

fun plotPieChartYear() {

}