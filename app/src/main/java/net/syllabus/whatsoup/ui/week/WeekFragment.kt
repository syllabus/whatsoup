package net.syllabus.whatsoup.ui.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import net.syllabus.whatsoup.databinding.FragmentWeekBinding

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

        val sat1View: TextView = binding.sat1
        homeViewModel.sat1.observe(viewLifecycleOwner) {
            sat1View.text = it
        }

        val sat2View: TextView = binding.sat2
        homeViewModel.sat2.observe(viewLifecycleOwner) {
            sat2View.text = it
        }

        val sun1View: TextView = binding.sun1
        homeViewModel.sun1.observe(viewLifecycleOwner) {
            sun1View.text = it
        }

        val sun2View: TextView = binding.sun2
        homeViewModel.sun2.observe(viewLifecycleOwner) {
            sun2View.text = it
        }

        val mon1View: TextView = binding.mon1
        homeViewModel.mon1.observe(viewLifecycleOwner) {
            mon1View.text = it
        }

        val mon2View: TextView = binding.mon2
        homeViewModel.mon2.observe(viewLifecycleOwner) {
            mon2View.text = it
        }

        val tue1View: TextView = binding.tue1
        homeViewModel.tue1.observe(viewLifecycleOwner) {
            tue1View.text = it
        }

        val tue2View: TextView = binding.tue2
        homeViewModel.tue2.observe(viewLifecycleOwner) {
            tue2View.text = it
        }

        val wed1View: TextView = binding.wed1
        homeViewModel.wed1.observe(viewLifecycleOwner) {
            wed1View.text = it
        }

        val wed2View: TextView = binding.wed2
        homeViewModel.wed2.observe(viewLifecycleOwner) {
            wed2View.text = it
        }

        val thu1View: TextView = binding.thu1
        homeViewModel.thu1.observe(viewLifecycleOwner) {
            thu1View.text = it
        }

        val thu2View: TextView = binding.thu2
        homeViewModel.thu2.observe(viewLifecycleOwner) {
            thu2View.text = it
        }

        val fri1View: TextView = binding.fri1
        homeViewModel.fri1.observe(viewLifecycleOwner) {
            fri1View.text = it
        }

        val fri2View: TextView = binding.fri2
        homeViewModel.fri2.observe(viewLifecycleOwner) {
            fri2View.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}