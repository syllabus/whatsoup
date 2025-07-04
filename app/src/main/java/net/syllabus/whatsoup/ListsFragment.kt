package net.syllabus.whatsoup

import android.app.Activity.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import net.syllabus.whatsoup.WeekFragment.WeekPlanAdapter
import net.syllabus.whatsoup.databinding.FragmentListsBinding
import net.syllabus.whatsoup.databinding.FragmentWeekBinding

class ListsFragment : Fragment() {

    private val sharedPrefName = "meal_prefs"
    private val mealListName = "meal_list"

    private lateinit var mealsRecyclerView: RecyclerView
    private lateinit var mealsAdapter: StringAdapter
    private lateinit var mealsAddButton: Button

    private lateinit var simplesRecyclerView: RecyclerView
    private lateinit var simplesAdapter: StringAdapter
    private lateinit var simplesAddButton: Button

    private lateinit var addonsRecyclerView: RecyclerView
    private lateinit var addonsAdapter: StringAdapter
    private lateinit var addonsAddButton: Button

    private lateinit var mealList: MealList

    private var _binding: FragmentListsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealsAddButton = requireView().findViewById(R.id.button_add_meal)
        mealsAddButton.setOnClickListener {
            Log.d("WHATSOUP", "CLICK ON ADD MEAL")
            mealList.plats.add("patates")
            mealsAdapter.updateData(mealList.plats)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()

        mealsRecyclerView = requireView().findViewById(R.id.mealRecyclerView)
        mealsRecyclerView.layoutManager = LinearLayoutManager(activity)
        mealsAdapter = StringAdapter()
        mealsRecyclerView.adapter = mealsAdapter

        mealsAdapter.updateData(mealList.plats)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveData(){
        val sharedPref = this.getActivity()?.getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        val editor = sharedPref?.edit()

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val jsonMealList = gson.toJson(mealList)
        editor?.putString(mealListName, jsonMealList)
        Log.d("WHATSOUP", "saving meal list " + jsonMealList)

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

    }


    inner class StringAdapter() : RecyclerView.Adapter<StringAdapter.StringViewHolder>() {

        private lateinit var displayedList: MutableList<String>

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): StringAdapter.StringViewHolder {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(
            holder: StringAdapter.StringViewHolder,
            position: Int
        ) {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            return displayedList.size
        }

        fun updateData(list: MutableList<String>){
            this.displayedList = list
            notifyDataSetChanged()
        }
        inner class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }
    }
}