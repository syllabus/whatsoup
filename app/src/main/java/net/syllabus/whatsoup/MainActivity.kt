package net.syllabus.whatsoup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder

class MainActivity : Activity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeekPlanAdapter
    private lateinit var settingsButton: Button
    private lateinit var generateButton: Button
    private lateinit var clearButton: Button
    private lateinit var saveButton: Button
    private lateinit var weekPlan: WeekPlan
    private val sharedPrefName = "meal_prefs"
    private var mealList: MealList = MealList.default()

    private val daysOfWeek = WeekPlan.defaultTemplate().getDays()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()

        recyclerView = findViewById(R.id.weekPlanRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WeekPlanAdapter()
        recyclerView.adapter = adapter

        settingsButton = findViewById(R.id.settings_button)
        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java).also{
                it.putExtra("meal_list", mealList)
            })
        }

        generateButton = findViewById(R.id.generate_button)
        generateButton.setOnClickListener {
            Log.d("WHATSOUP", "CLICK ON GENERATE")
            weekPlan = mealList.randomWeek(WeekPlan.defaultTemplate())
            Log.d("WHATSOUP", "NEW PLAN: " + weekPlan)
            adapter.updateData(daysOfWeek, mealList, weekPlan)
            Log.d("WHATSOUP", "GENERATE IS DONE")
        }

        clearButton = findViewById(R.id.clear_button)
        clearButton.setOnClickListener {
            mealList = MealList.default()
            weekPlan = mealList.randomWeek(WeekPlan.defaultTemplate())
            adapter.updateData(daysOfWeek, mealList, weekPlan)
        }

        saveButton = findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            saveData()
        }

        adapter.updateData(daysOfWeek, mealList, weekPlan)
    }

    override fun onResume() {
        super.onResume()
        loadData()
        adapter.updateData(daysOfWeek, mealList, weekPlan)
    }
    private fun saveData(){
        val sharedPref = getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        val editor = sharedPref.edit()

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val jsonMeals = gson.toJson(mealList)
        editor.putString("meal_list", jsonMeals)
        Log.d("WHATSOUP", "saving meals " + jsonMeals)

        val jsonWeekPlan = gson.toJson(weekPlan)
        editor.putString("week_plan", jsonWeekPlan)
        Log.d("WHATSOUP", "saving plan " + jsonWeekPlan)

        editor.apply()
    }
    private fun loadData() {
        val sharedPref = getSharedPreferences(sharedPrefName, MODE_PRIVATE)

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val jsonMeals = sharedPref.getString("meal_list", null)
        if (jsonMeals != null) {
            val type = com.google.gson.reflect.TypeToken.getParameterized(MutableList::class.java, MealList::class.java).type
            try {
                mealList = gson.fromJson(jsonMeals, type)
                Log.d("WHATSOUP", "loaded meals " + jsonMeals)
            } catch (e: Exception) {
                e.printStackTrace()
                mealList = MealList.default()
            }
        } else {
            mealList = MealList.default()
        }

        val jsonWeekPlan = sharedPref.getString("week_plan", null)
        if(jsonWeekPlan != null){
            try {
                weekPlan = gson.fromJson(jsonWeekPlan, WeekPlan::class.java)
                Log.d("WHATSOUP", "loaded plan " + jsonWeekPlan)
            } catch (e: Exception) {
                e.printStackTrace()
                weekPlan = mealList.randomWeek(WeekPlan.defaultTemplate())
            }
        } else {
            weekPlan = mealList.randomWeek(WeekPlan.defaultTemplate())
        }
    }

    inner class WeekPlanAdapter() : RecyclerView.Adapter<WeekPlanAdapter.ItemViewHolder>() {

        private var daysOfWeek: List<String> = WeekPlan.defaultTemplate().getDays()
        private var mealList: MealList = MealList.default()
        private var weekPlan : WeekPlan = mealList.randomWeek(WeekPlan.defaultTemplate())
        private var preventEvents : Boolean = false

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ItemViewHolder {
            val view = layoutInflater.inflate(R.layout.week_plan_item, parent, false)
            Log.d("WHATSOUP", "Adding ItemHolder " + viewType)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            Log.d("WHATSOUP", "Binding ItemHolder " + position)
            val day = daysOfWeek[position]
            holder.dayTextView.text = day

            preventEvents = true
            val currentLunch = weekPlan.getMeal(WeekPlan.PairKey(day, true))
            holder.lunchEditText.setText(currentLunch?.name)

            val currentDinner = weekPlan.getMeal(WeekPlan.PairKey(day, false))
            holder.dinnerEditText.setText(currentDinner?.name)
            preventEvents = false

            Log.d("WHATSOUP", "Display " + day + ": " + currentLunch + " AND " + currentDinner)

            holder.lunchEditText.doAfterTextChanged {
                if (!preventEvents) {
                    weekPlan.setMeal(
                        WeekPlan.PairKey(day, true),
                        Meal(Meal.MealType.HARDCODED, it.toString())
                    )
                    Log.d("WHATSOUP", "Save " + day + " lunch: " + it.toString())
                }
            }

            holder.dinnerEditText.doAfterTextChanged {
                if (!preventEvents) {
                    weekPlan.setMeal(
                        WeekPlan.PairKey(day, false),
                        Meal(Meal.MealType.HARDCODED, it.toString())
                    )
                    Log.d("WHATSOUP", "Save " + day + " dinner: " + it.toString())
                }
            }


        }

        override fun getItemCount(): Int {
            return daysOfWeek.size
        }
        fun updateData(daysOfWeek: List<String>, mealList: MealList, weekPlan: WeekPlan){
            this.daysOfWeek = daysOfWeek
            this.mealList = mealList
            this.weekPlan = weekPlan
            notifyDataSetChanged()

        }
        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val dayTextView: TextView = itemView.findViewById(R.id.dayTextView)
            val lunchEditText: EditText = itemView.findViewById(R.id.lunchEditText)
            val dinnerEditText: EditText = itemView.findViewById(R.id.dinnerEditText)
        }
    }
}
