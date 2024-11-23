package net.syllabus.whatsoup.ui.week

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import net.syllabus.whatsoup.NextPlatWidget
import net.syllabus.whatsoup.TodayWidget2
import net.syllabus.whatsoup.databinding.FragmentWeekBinding
import net.syllabus.whatsoup.model.WeekTemplate

class WeekFragment : Fragment() {

    private var _binding: FragmentWeekBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(WeekViewModel::class.java)

        _binding = FragmentWeekBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnGenerate.setOnClickListener({
            val toast = Toast.makeText(context, "en cours de calcul...", 3)
            toast.show()
            generate(homeViewModel)
        });

        binding.btnSave.setOnClickListener({
            val toast = Toast.makeText(context, "c'est en train de save...", 3)
            toast.show()
            context?.openFileOutput("plats.txt", Context.MODE_PRIVATE).use { it?.write(homeViewModel.toString().toByteArray(Charsets.UTF_8))}

            updateWidgets(TodayWidget2::class.java)
            updateWidgets(NextPlatWidget::class.java)
        });

        binding.btnLoad.setOnClickListener({
            val toast = Toast.makeText(context, "ça charge...", 3)
            toast.show()
            context?.openFileInput("plats.txt").use { stream ->
                stream?.bufferedReader().use { homeViewModel.fromString(it?.readText().toString()) }
            }
        });

        val map = mapOf(

            binding.sat1 to homeViewModel.sat1,
            binding.sat2 to homeViewModel.sat2,
            binding.sun1 to homeViewModel.sun1,
            binding.sun2 to homeViewModel.sun2,
            binding.mon1 to homeViewModel.mon1,
            binding.mon2 to homeViewModel.mon2,
            binding.tue1 to homeViewModel.tue1,
            binding.tue2 to homeViewModel.tue2,
            binding.wed1 to homeViewModel.wed1,
            binding.wed2 to homeViewModel.wed2,
            binding.thu1 to homeViewModel.thu1,
            binding.thu2 to homeViewModel.thu2,
            binding.fri1 to homeViewModel.fri1,
            binding.fri2 to homeViewModel.fri2,
        )

        map.forEach { view, model ->
            val editTextView: EditText = view
            model.observe(viewLifecycleOwner) {
                if (!editTextView.getText().toString().equals(it)) {
                    editTextView.setText(it)
                }
            }
            editTextView.doOnTextChanged { text, start, before, count ->
                model.value = text.toString()
            }
        }

        return root
    }

    fun updateWidgets(clazz : Class<*>) {
        val intent: Intent = Intent(
            context,
            clazz
        )
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE")
        val ids = AppWidgetManager.getInstance(this.context).getAppWidgetIds(
            ComponentName(
                requireContext(),
                clazz
            )
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context?.sendBroadcast(intent)
    }

    fun generate(homeViewModel : WeekViewModel) {
        val simples = mutableListOf(
            "Knackies",
            "Steaks hachés",
            "Poissons Panés",
            "Oeufs",
            "Saucisses",
            "Escalopes de poulet / dinde",
            "Cordons bleus",
            "Buns",
            "Jambon"
        );

        val avec = mutableListOf(
            "Haricots verts",
            "Petits pois / Carottes",
            "Epinards / Poireaux surgelés",
            "Galette de légumes",
            "Ratatouille",
            "Carottes rapées",
            "Courgettes",
            "Frites",
            "Purée",
            "Semoule",
            "Quinoa",
            "Polenta",
            "Riz"
        );

        var plats = mutableListOf(
            "Paupiettes / Riz",
            "Old El Paso",
            "Semoule / Ratatouille",
            "Boeuf bourguignon",
            "Pot au feu",
            "Quenelles / riz",
            "Gratin (chou-fleur,endives-jambon,courge...)",
            "Sushis, raclette, fondue, tartiflette",
            "Choucroute",
            "Saumon / Riz",
            "Cassoulet",
            "Piperade",
            "Croques-monsieurs",
            "Petit salé aux lentilles"
        );

        homeViewModel.onGenerate(simples, avec, plats, WeekTemplate.BASIC)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}