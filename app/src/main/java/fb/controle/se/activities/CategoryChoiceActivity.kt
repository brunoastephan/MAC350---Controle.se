package fb.controle.se.activities

import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fb.controle.se.R
import fb.controle.se.database.DatabaseContract
import fb.controle.se.database.DbCategoryReader

class CategoryChoiceActivity : AppCompatActivity() {
    private lateinit var dbCategoryReader: DbCategoryReader
    private lateinit var scroller: ScrollView
    private lateinit var radioGroup: RadioGroup
    private lateinit var selectCategoryButton: Button

    private fun createCategoryButton(category: Map<String, Any>): RadioButton {
        val categoryButton = RadioButton(this)
        categoryButton.layoutParams = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT,
            RadioGroup.LayoutParams.MATCH_PARENT
        )
        categoryButton.text = getString(R.string.category_button_template).format(category[DatabaseContract.CategoriesEntry.COLUMN_ICON], category[DatabaseContract.CategoriesEntry.COLUMN_NAME])
        categoryButton.textSize = 35F
        return categoryButton
    }

    private fun createCategoryList(): RadioGroup {
        val categoryList = RadioGroup(this)
        categoryList.layoutParams = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.WRAP_CONTENT,
            RadioGroup.LayoutParams.WRAP_CONTENT
        )

        return categoryList
    }

    private fun addCategoriesRadioGroup(radioGroup: RadioGroup) {
        val categories = dbCategoryReader.readCategories()
        for (category in categories) {
            radioGroup.addView(createCategoryButton(category))
        }
    }

    private fun getSelectedCategory(categories: List<Map<String, Any>>, checkedRadioButtonId: Int): Int{
        val firstChild = radioGroup.getChildAt(0)
        val firstId = firstChild.id
        val selectedCategoryIndex = checkedRadioButtonId - firstId
        return categories[selectedCategoryIndex][BaseColumns._ID].toString().toInt()
    }
    private fun selectCategoryButtonClick() {
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId
        if (checkedRadioButtonId == -1) {
            Toast.makeText(this, getString(R.string.no_category_selected_warning), Toast.LENGTH_SHORT).show()
            return
        }
        val categories = dbCategoryReader.readCategories()
        val selectedCategory = getSelectedCategory(categories, checkedRadioButtonId)

        val intent = Intent(this, NewExpenseActivity::class.java)
        intent.putExtra("selectedCategory", selectedCategory.toString())
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_choice)

        dbCategoryReader = DbCategoryReader(this)

        scroller = findViewById(R.id.category_list_scroller)
        radioGroup = createCategoryList()
        scroller.addView(radioGroup)

        selectCategoryButton = findViewById(R.id.select_category_button)
        selectCategoryButton.setOnClickListener { selectCategoryButtonClick() }
        addCategoriesRadioGroup(radioGroup)

        supportActionBar?.hide()
    }
}