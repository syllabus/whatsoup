package net.syllabus.whatsoup

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.gson.Gson


class SettingsActivity : Activity() {

    private lateinit var newMealName: EditText
    private lateinit var addMealButton: Button
    private lateinit var mealsListView: ListView
    private lateinit var backButton: Button
    private lateinit var mealList: MealList
    private val sharedPrefName = "meal_prefs"
    private lateinit var arrayAdapter : ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        newMealName = findViewById(R.id.new_meal_name)
        addMealButton = findViewById(R.id.add_meal_button)
        mealsListView = findViewById(R.id.meals_list_view)
        backButton = findViewById(R.id.back_button)

        val parcelable = intent.getSerializableExtra("meal_list")
        if(parcelable != null){
            mealList = parcelable as MealList
        } else {
            mealList = MealList.default()
        }


        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mealList.plats)
        mealsListView.adapter = arrayAdapter

        addMealButton.setOnClickListener {
            val mealName = newMealName.text.toString().trim()
            if (mealName.isNotEmpty()) {
                mealList.plats.add(mealName)
                newMealName.text.clear()
                saveData()
                arrayAdapter.notifyDataSetChanged()
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }
    private fun saveData() {
        val sharedPref = getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val jsonMeals = gson.toJson(mealList)
        editor.putString("meal_list", jsonMeals)
        editor.apply()
    }
}
