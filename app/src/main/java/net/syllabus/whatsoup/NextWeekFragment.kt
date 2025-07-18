package net.syllabus.whatsoup

import android.app.Activity.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import net.syllabus.whatsoup.databinding.FragmentNextweekBinding

class NextWeekFragment : Fragment() {

    private val sharedPrefName = "meal_prefs"
    private val planName = "week_plan"
    private val nextplanName = "nextweek_plan"
    private val templateName = "week_template"
    private val mealListName = "meal_list"

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeekPlanAdapter
    private lateinit var generateButton: Button
    private lateinit var saveButton: Button
    private lateinit var loadButton: Button

    private lateinit var mealList: MealList
    private lateinit var template: WeekPlan
    private lateinit var weekPlan: WeekPlan

    private var _binding: FragmentNextweekBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNextweekBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generateButton = requireView().findViewById(R.id.button_generate)
        generateButton.setOnClickListener {
            Log.d("WHATSOUP", "CLICK ON GENERATE")
            weekPlan = mealList.randomWeek(template)
            Log.d("WHATSOUP", "NEW PLAN: " + weekPlan)
            adapter.updateData(weekPlan)
            Log.d("WHATSOUP", "GENERATE IS DONE")
        }

        saveButton = requireView().findViewById(R.id.button_setweek)
        saveButton.setOnClickListener {
            Log.d("WHATSOUP", "CLICK ON SAVE")
            val sharedPref = this.getActivity()?.getSharedPreferences(sharedPrefName, MODE_PRIVATE)
            val editor = sharedPref?.edit()

            val gson = GsonBuilder().enableComplexMapKeySerialization().create()
            val jsonWeekPlan = gson.toJson(weekPlan)
            editor?.putString(planName, jsonWeekPlan)
            Log.d("WHATSOUP", "saving plan " + jsonWeekPlan)

            editor?.apply()
            Log.d("WHATSOUP", "SAVE IS DONE")
        }

        loadButton = requireView().findViewById(R.id.button_getweek)
        loadButton.setOnClickListener {
            Log.d("WHATSOUP", "CLICK ON LOAD")
            val sharedPref = this.getActivity()?.getSharedPreferences(sharedPrefName, MODE_PRIVATE)
            val gson = GsonBuilder().enableComplexMapKeySerialization().create()
            val jsonWeekPlan = sharedPref?.getString(planName, null)
            val type = WeekPlan::class.java
            weekPlan = gson.fromJson(jsonWeekPlan, type)
            adapter.updateData(weekPlan)
            Log.d("WHATSOUP", "LOAD IS DONE")
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()

        recyclerView = requireView().findViewById(R.id.weekPlanRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = WeekPlanAdapter()
        recyclerView.adapter = adapter

        adapter.updateData(weekPlan)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveData(){
        val sharedPref = this.getActivity()?.getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        val editor = sharedPref?.edit()

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val jsonWeekPlan = gson.toJson(weekPlan)
        editor?.putString(nextplanName, jsonWeekPlan)
        Log.d("WHATSOUP", "saving plan " + jsonWeekPlan)

        editor?.apply()
    }

    private fun loadData() {
        val sharedPref = this.getActivity()?.getSharedPreferences(sharedPrefName, MODE_PRIVATE)

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val jsonMeals = sharedPref?.getString(mealListName, null)
        if (jsonMeals != null) {
            val type = MealList::class.java
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

        val jsonTemplate = sharedPref?.getString(templateName, null)
        if (jsonTemplate != null) {
            val type = WeekPlan::class.java
            try {
                template = gson.fromJson(jsonTemplate, type)
                Log.d("WHATSOUP", "loaded template " + jsonTemplate)
            } catch (e: Exception) {
                e.printStackTrace()
                template = WeekPlan.defaultTemplate()
            }
        } else {
            template = WeekPlan.defaultTemplate()
        }

        val jsonWeekPlan = sharedPref?.getString(nextplanName, null)
        if(jsonWeekPlan != null){
            val type = WeekPlan::class.java
            try {
                weekPlan = gson.fromJson(jsonWeekPlan, type)
                Log.d("WHATSOUP", "loaded plan " + jsonWeekPlan)
            } catch (e: Exception) {
                e.printStackTrace()
                weekPlan = mealList.randomWeek(template)
            }
        } else {
            weekPlan = mealList.randomWeek(template)
        }

    }


    inner class WeekPlanAdapter() : RecyclerView.Adapter<WeekPlanAdapter.ItemViewHolder>() {

        private var displayedWeekPlan : WeekPlan = WeekPlan()

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): WeekPlanAdapter.ItemViewHolder {
            val view = layoutInflater.inflate(R.layout.nextweek_plan_item, parent, false)
            Log.d("WHATSOUP", "Adding ItemHolder " + viewType)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: WeekPlanAdapter.ItemViewHolder, position: Int) {
            Log.d("WHATSOUP", "Binding ItemHolder " + position)
            val day = displayedWeekPlan.getDays()[position]
            holder.dayTextView.text = day

            val currentLunch = displayedWeekPlan.getMeal(WeekPlan.PairKey(day, true))
            holder.lunchEditText.setText(currentLunch?.name)

            val currentDinner = displayedWeekPlan.getMeal(WeekPlan.PairKey(day, false))
            holder.dinnerEditText.setText(currentDinner?.name)

            Log.d("WHATSOUP", "Display " + day + ": " + currentLunch + " AND " + currentDinner)

        }

        override fun getItemCount(): Int {
            return displayedWeekPlan.getDays().size
        }

        fun updateData(weekPlan: WeekPlan){
            this.displayedWeekPlan = weekPlan
            notifyDataSetChanged()
        }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val dayTextView: TextView = itemView.findViewById(R.id.dayTextView)
            val lunchEditText: EditText = itemView.findViewById(R.id.lunchEditText)
            val lunchGene: Button = itemView.findViewById(R.id.lunchGenerateMeal)
            val lunchRate: Button = itemView.findViewById(R.id.lunchGenerateComposition)
            val dinnerEditText: EditText = itemView.findViewById(R.id.dinnerEditText)
            val dinnerGene: Button = itemView.findViewById(R.id.dinnerGenerateMeal)
            val dinnerRate: Button = itemView.findViewById(R.id.dinnerGenerateComposition)

            init {
                lunchEditText?.doAfterTextChanged {
                    val d = dayTextView.text.toString()
                    Log.d("WHATSOUP", "text changed for lunch " + d)
                    displayedWeekPlan.setMeal(
                        WeekPlan.PairKey(d, true),
                        Meal(Meal.MealType.HARDCODED, it.toString())
                    )
                    Log.d("WHATSOUP", "Save " + d + " lunch: " + it.toString())
                    saveData()
                }
                dinnerEditText?.doAfterTextChanged {
                    val d = dayTextView.text.toString()
                    Log.d("WHATSOUP", "text changed for dinner " + d)
                    displayedWeekPlan.setMeal(
                        WeekPlan.PairKey(d, false),
                        Meal(Meal.MealType.HARDCODED, it.toString())
                    )
                    Log.d("WHATSOUP", "Save " + d + " dinner: " + it.toString())
                    saveData()
                }
                lunchGene.setOnClickListener { lunchEditText.setText(mealList.random(Meal.MealType.MEAL)) }
                lunchRate.setOnClickListener { lunchEditText.setText(mealList.random(Meal.MealType.BUILT)) }
                dinnerGene.setOnClickListener { dinnerEditText.setText(mealList.random(Meal.MealType.MEAL)) }
                dinnerRate.setOnClickListener { dinnerEditText.setText(mealList.random(Meal.MealType.BUILT)) }
            }
        }
    }
}