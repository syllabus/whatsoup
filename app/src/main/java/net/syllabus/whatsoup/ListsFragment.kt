package net.syllabus.whatsoup

import android.app.Activity.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import net.syllabus.whatsoup.WeekFragment.WeekPlanAdapter
import net.syllabus.whatsoup.databinding.FragmentListsBinding
import net.syllabus.whatsoup.databinding.FragmentWeekBinding

class ListsFragment : Fragment() {

    private val sharedPrefName = "meal_prefs"
    private val mealListName = "meal_list"

    private lateinit var resetButton: Button

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
            mealList.plats.add("beaujolais nouveau")
            mealsAdapter.updateData(mealList.plats)
        }

        simplesAddButton = requireView().findViewById(R.id.button_add_simple)
        simplesAddButton.setOnClickListener {
            Log.d("WHATSOUP", "CLICK ON ADD SIMPLE")
            mealList.simples.add("patates nouvelles")
            simplesAdapter.updateData(mealList.simples)
        }

        addonsAddButton = requireView().findViewById(R.id.button_add_avec)
        addonsAddButton.setOnClickListener {
            Log.d("WHATSOUP", "CLICK ON ADD ADDON")
            mealList.avec.add("pousses de bambou")
            addonsAdapter.updateData(mealList.avec)
        }

        resetButton = requireView().findViewById(R.id.button_reset)
        resetButton.setOnClickListener {
            Log.d("WHATSOUP", "CLICK ON RESET MEAL")
            resetData()
            loadData()
            mealsAdapter.updateData(mealList.plats)
            simplesAdapter.updateData(mealList.simples)
            addonsAdapter.updateData(mealList.avec)
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

        simplesRecyclerView = requireView().findViewById(R.id.simpleRecyclerView)
        simplesRecyclerView.layoutManager = LinearLayoutManager(activity)
        simplesAdapter = StringAdapter()
        simplesRecyclerView.adapter = simplesAdapter

        simplesAdapter.updateData(mealList.simples)

        addonsRecyclerView = requireView().findViewById(R.id.avecRecyclerView)
        addonsRecyclerView.layoutManager = LinearLayoutManager(activity)
        addonsAdapter = StringAdapter()
        addonsRecyclerView.adapter = addonsAdapter

        addonsAdapter.updateData(mealList.avec)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun resetData(){
        val sharedPref = this.getActivity()?.getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        val editor = sharedPref?.edit()

        editor?.remove(mealListName)
        Log.d("WHATSOUP", "reseting meal list ")

        editor?.apply()
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
            val type = MealList::class.java
            try {
                Log.d("WHATSOUP", "parsing meal list " + jsonMeals)
                mealList = gson.fromJson(jsonMeals, type)
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
            val view = layoutInflater.inflate(R.layout.list_item, parent, false)
            Log.d("WHATSOUP", "Adding Item " + viewType)
            return StringViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: StringAdapter.StringViewHolder,
            position: Int
        ) {
            Log.d("WHATSOUP", "Binding ItemHolder " + position)
            val string = displayedList.get(position)
            holder.stringEditText.setText(string)
            holder.pos = position

            Log.d("WHATSOUP", "Display " + string)
        }

        override fun getItemCount(): Int {
            return displayedList.size
        }

        fun updateData(list: MutableList<String>){
            this.displayedList = list
            notifyDataSetChanged()
        }
        inner class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var pos : Int = -1
            val stringEditText: EditText = itemView.findViewById(R.id.stringEditText)
            val removeButton: Button = itemView.findViewById(R.id.removeButton)

            init {
                stringEditText.doAfterTextChanged {
                    if (pos >= 0) {
                        Log.d("WHATSOUP", "text " + pos + " changed " + it.toString())
                        displayedList.set(pos, it.toString())
                        Log.d("WHATSOUP", "Save " + pos + " string: " + it.toString())
                        saveData()
                    }
                }
                removeButton.setOnClickListener {
                    if (pos >= 0) {
                        Log.d("WHATSOUP", "remove " + pos)
                        displayedList.removeAt(pos)
                        Log.d("WHATSOUP", "Save " + pos)
                        saveData()
                        mealsAdapter.updateData(mealList.plats)
                        simplesAdapter.updateData(mealList.simples)
                        addonsAdapter.updateData(mealList.avec)
                    }
                }
            }
        }
    }
}