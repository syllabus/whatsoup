package net.syllabus.whatsoup

import android.app.Activity.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import net.syllabus.whatsoup.databinding.FragmentTemplateBinding
import net.syllabus.whatsoup.databinding.FragmentWeekBinding

class TemplateFragment : Fragment() {

    private val sharedPrefName = "meal_prefs"
    private val templateName = "week_template"

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TemplateAdapter
    private lateinit var resetButton: Button
    private lateinit var shareButton: Button

    private lateinit var template: WeekPlan

    private var _binding: FragmentTemplateBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTemplateBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetButton = requireView().findViewById(R.id.button_default)
        resetButton.setOnClickListener {
            Log.d("WHATSOUP", "CLICK ON RESET")
            resetData()
            loadData()
            Log.d("WHATSOUP", "DEFAULT TEMPLATE: " + template)
            adapter.updateData(template)
            Log.d("WHATSOUP", "RESET IS DONE")
        }
        shareButton = requireView().findViewById(R.id.button_share)
        shareButton.setOnClickListener {
            Log.d("WHATSOUP", "CLICK ON SHARE")
            val gson = GsonBuilder().enableComplexMapKeySerialization().create()
            val jsonWeekPlan = gson.toJson(template)
            val sharingIntent = Intent(Intent.ACTION_SEND)
            //sharingIntent.setType("text/whatsoup-menu")
            //sharingIntent.setType("application/prs.implied-object+json")
            sharingIntent.setType("text/x-pascal")
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"weekplan.whatsou.p")
            sharingIntent.putExtra(Intent.EXTRA_TEXT,jsonWeekPlan)
            startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }    }

    override fun onResume() {
        super.onResume()
        loadData()

        recyclerView = requireView().findViewById(R.id.templateRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = TemplateAdapter()
        recyclerView.adapter = adapter

        adapter.updateData(template)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveData() {
        val sharedPref = this.getActivity()?.getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        val editor = sharedPref?.edit()

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()

        val jsonTemplate = gson.toJson(template)
        editor?.putString(templateName, jsonTemplate)
        Log.d("WHATSOUP", "saving template " + jsonTemplate)

        editor?.apply()
    }

    private fun resetData() {
        val sharedPref = this.getActivity()?.getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        val editor = sharedPref?.edit()

        editor?.remove(templateName)
        editor?.apply()
    }

    private fun loadData() {
        val sharedPref = this.getActivity()?.getSharedPreferences(sharedPrefName, MODE_PRIVATE)

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
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

    }

    inner class TemplateAdapter() : RecyclerView.Adapter<TemplateAdapter.ItemViewHolder>() {

        private var displayedTemplate: WeekPlan = WeekPlan()

        override fun onCreateViewHolder(
            parent: android.view.ViewGroup,
            viewType: Int
        ): ItemViewHolder {
            val view = layoutInflater.inflate(R.layout.template_item, parent, false)
            Log.d("WHATSOUP", "Adding ItemHolder " + viewType)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            Log.d("WHATSOUP", "Binding ItemHolder " + position)
            val day = displayedTemplate.getDays()[position]
            holder.dayTextView.setText(day)

            val currentLunch = displayedTemplate.getMeal(WeekPlan.PairKey(day, true))
            holder.lunchSpinner?.setSelection(currentLunch?.type!!.ordinal)
            holder.lunchText?.setEnabled(currentLunch?.type!!.ordinal == Meal.MealType.HARDCODED.ordinal)
            holder.lunchText?.setText(currentLunch?.name)

            val currentDinner = displayedTemplate.getMeal(WeekPlan.PairKey(day, false))
            holder.dinnerSpinner?.setSelection(currentDinner?.type!!.ordinal)
            holder.dinnerText?.setEnabled(currentDinner?.type!!.ordinal == Meal.MealType.HARDCODED.ordinal)
            holder.dinnerText?.setText(currentDinner?.name)

            Log.d("WHATSOUP", "Display " + day + ": " + currentLunch + " AND " + currentDinner)

        }

        override fun getItemCount(): Int {
            return displayedTemplate.getDays().size
        }

        fun updateData(aTemplate: WeekPlan) {
            this.displayedTemplate = aTemplate
            notifyDataSetChanged()
        }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val dayTextView: EditText = itemView.findViewById(R.id.dayText)
            val lunchSpinner: Spinner = itemView.findViewById(R.id.lunchSpinner)
            val lunchText: EditText = itemView.findViewById(R.id.lunchText)
            val dinnerSpinner: Spinner = itemView.findViewById(R.id.dinnerSpinner)
            val dinnerText: EditText = itemView.findViewById(R.id.dinnerText)

            init {
                lunchSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        lunchText?.setEnabled(id == Meal.MealType.HARDCODED.ordinal.toLong())
                        val d = dayTextView.text.toString()
                        Log.d("WHATSOUP", "spinner changed for lunch " + d)
                        lunchText?.setText(displayedTemplate.getMeal(WeekPlan.PairKey(d, true))?.name)
                        Log.d("WHATSOUP", "Save " + d + " lunch")
                        saveData()
                    }
                }
                dinnerSpinner?.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            dinnerText?.setEnabled(id == Meal.MealType.HARDCODED.ordinal.toLong())
                            val d = dayTextView.text.toString()
                            Log.d("WHATSOUP", "spinner changed for dinner " + d)
                            dinnerText?.setText(displayedTemplate.getMeal(WeekPlan.PairKey(d, false))?.name)
                            Log.d("WHATSOUP", "Save " + d + " dinner")
                            saveData()
                        }
                    }
                lunchText?.doAfterTextChanged {
                    val d = dayTextView.text.toString()
                    Log.d("WHATSOUP", "text changed for lunch " + d)
                    displayedTemplate.setMeal(
                        WeekPlan.PairKey(d, true),
                        Meal(Meal.MealType.values()[lunchSpinner.selectedItemPosition], it.toString())
                    )
                    Log.d("WHATSOUP", "Save " + d + " lunch: " + it.toString())
                    saveData()
                }
                dinnerText?.doAfterTextChanged {
                    val d = dayTextView.text.toString()
                    Log.d("WHATSOUP", "text changed for dinner " + d)
                    displayedTemplate.setMeal(
                        WeekPlan.PairKey(d, false),
                        Meal(Meal.MealType.values()[dinnerSpinner.selectedItemPosition], it.toString())
                    )
                    Log.d("WHATSOUP", "Save " + d + " dinner: " + it.toString())
                    saveData()
                }
            }
        }
    }
}