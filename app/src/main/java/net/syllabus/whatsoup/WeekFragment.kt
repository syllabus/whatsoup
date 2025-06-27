package net.syllabus.whatsoup

import android.app.Activity.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import net.syllabus.whatsoup.databinding.FragmentWeekBinding

class WeekFragment : Fragment() {

    private val sharedPrefName = "meal_prefs"
    private val planName = "week_plan"
    private val templateName = "week_template"
    private val mealListName = "meal_list"

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeekPlanAdapter

    private lateinit var mealList: MealList
    private lateinit var template: WeekPlan
    private lateinit var weekPlan: WeekPlan

    private var _binding: FragmentWeekBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWeekBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
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
        editor?.putString(planName, jsonWeekPlan)
        Log.d("WHATSOUP", "saving plan " + jsonWeekPlan)

        editor?.apply()
    }

    private fun loadData() {
        val sharedPref = this.getActivity()?.getSharedPreferences(sharedPrefName, MODE_PRIVATE)

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val jsonMeals = sharedPref?.getString(mealListName, null)
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

        val jsonTemplate = sharedPref?.getString(templateName, null)
        if (jsonTemplate != null) {
            val type = com.google.gson.reflect.TypeToken.getParameterized(MutableList::class.java, MealList::class.java).type
            try {
                template = gson.fromJson(jsonTemplate, type)
                Log.d("WHATSOUP", "loaded template " + jsonTemplate)
            } catch (e: Exception) {
                e.printStackTrace()
                template = WeekPlan.defaultTemplate()
            }
        } else {
            weekPlan = WeekPlan.defaultTemplate()
        }

        val jsonWeekPlan = sharedPref?.getString(planName, null)
        if(jsonWeekPlan != null){
            try {
                weekPlan = gson.fromJson(jsonWeekPlan, WeekPlan::class.java)
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
            val view = layoutInflater.inflate(R.layout.week_plan_item, parent, false)
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
            val dinnerEditText: EditText = itemView.findViewById(R.id.dinnerEditText)

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
            }
        }
    }
}