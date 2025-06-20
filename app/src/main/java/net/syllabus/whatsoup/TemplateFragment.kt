package net.syllabus.whatsoup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.syllabus.whatsoup.MainActivity.WeekPlanAdapter
import net.syllabus.whatsoup.MainActivity.WeekPlanAdapter.ItemViewHolder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TemplateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TemplateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeekPlanAdapter
    private val sharedPrefName = "meal_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setContentView(R.layout.activity_main)

        loadData()

        recyclerView = view?.findViewById(R.id.templateRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WeekPlanAdapter()
        recyclerView.adapter = adapter

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_template, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TemplateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TemplateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    inner class TemplateAdapter() : RecyclerView.Adapter<TemplateAdapter.ItemViewHolder>() {

        private var displayedTemplate : WeekPlan = WeekPlan()

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ItemViewHolder {
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

        fun updateData(weekPlan: WeekPlan){
            this.displayedTemplate = weekPlan
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
                        lunchText?.visibility = if (id == 2L) View.VISIBLE else View.INVISIBLE
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
                            dinnerText?.visibility = if (id == 2L) View.VISIBLE else View.INVISIBLE
                        }
                    }
            }
        }
    }
}