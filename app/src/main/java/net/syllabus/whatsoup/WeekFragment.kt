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
import net.syllabus.whatsoup.databinding.FragmentWeekBinding

class WeekFragment : Fragment() {

    private val sharedPrefName = "meal_prefs"
    private val planName = "week_plan"

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeekPlanAdapter
    private lateinit var shareButton: Button

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shareButton = requireView().findViewById(R.id.button_share)
        shareButton.setOnClickListener {
            Log.d("WHATSOUP", "CLICK ON SHARE")
            // TODO
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

    private fun loadData() {
        val sharedPref = this.getActivity()?.getSharedPreferences(sharedPrefName, MODE_PRIVATE)

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val jsonWeekPlan = sharedPref?.getString(planName, null)
        if(jsonWeekPlan != null){
            val type = WeekPlan::class.java
            try {
                weekPlan = gson.fromJson(jsonWeekPlan, type)
                Log.d("WHATSOUP", "loaded plan " + jsonWeekPlan)
            } catch (e: Exception) {
                e.printStackTrace()
                weekPlan = MealList.default().randomWeek(WeekPlan.defaultTemplate())
            }
        } else {
            weekPlan = MealList.default().randomWeek(WeekPlan.defaultTemplate())
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
            holder.lunchTextView.setText(currentLunch?.name)

            val currentDinner = displayedWeekPlan.getMeal(WeekPlan.PairKey(day, false))
            holder.dinnerTextView.setText(currentDinner?.name)

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
            val lunchTextView: TextView = itemView.findViewById(R.id.lunchTextView)
            val dinnerTextView: TextView = itemView.findViewById(R.id.dinnerTextView)
        }
    }
}