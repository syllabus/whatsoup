package net.syllabus.whatsoup

import android.app.Activity.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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

    private fun loadData() {
        val sharedPref = this.getActivity()?.getSharedPreferences(sharedPrefName, MODE_PRIVATE)

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val jsonTemplate = sharedPref?.getString(templateName, null)
        if (jsonTemplate != null) {
            val type = com.google.gson.reflect.TypeToken.getParameterized(
                MutableList::class.java,
                MealList::class.java
            ).type
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
            holder.lunchText?.setText(currentLunch?.name)

            val currentDinner = displayedTemplate.getMeal(WeekPlan.PairKey(day, false))
            holder.dinnerSpinner?.setSelection(currentLunch?.type!!.ordinal)
            holder.dinnerText?.setText(currentLunch?.name)

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
            val lunchSpinner: Spinner? = view?.findViewById(R.id.lunchSpinner)
            val lunchText: EditText? = view?.findViewById(R.id.lunchText)
            val dinnerSpinner: Spinner? = view?.findViewById(R.id.dinnerSpinner)
            val dinnerText: EditText? = view?.findViewById(R.id.lunchText)

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
                        lunchText?.setEnabled(id == 2L)
                        val d = dayTextView.text.toString()
                        Log.d("WHATSOUP", "spinner changed for lunch " + d)
                        displayedTemplate.setMeal(
                            WeekPlan.PairKey(d, true),
                            Meal(Meal.MealType.entries[id.toInt()], "patates")
                        )
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
                            dinnerText?.setEnabled(id == 2L)
                            val d = dayTextView.text.toString()
                            Log.d("WHATSOUP", "spinner changed for dinner " + d)
                            displayedTemplate.setMeal(
                                WeekPlan.PairKey(d, false),
                                Meal(Meal.MealType.entries[id.toInt()], "patates")
                            )
                            Log.d("WHATSOUP", "Save " + d + " dinner")
                            saveData()
                        }
                    }
                lunchText?.doAfterTextChanged {
                    val d = dayTextView.text.toString()
                    Log.d("WHATSOUP", "text changed for lunch " + d)
                    displayedTemplate.setMeal(
                        WeekPlan.PairKey(d, true),
                        Meal(Meal.MealType.HARDCODED, it.toString())
                    )
                    Log.d("WHATSOUP", "Save " + d + " lunch: " + it.toString())
                    saveData()
                }
                dinnerText?.doAfterTextChanged {
                    val d = dayTextView.text.toString()
                    Log.d("WHATSOUP", "text changed for dinner " + d)
                    displayedTemplate.setMeal(
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