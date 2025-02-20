package com.umc.sweepic.presentation.record.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R
import com.umc.sweepic.presentation.record.Adapter.HistoryTagAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class HistoryTagFragment : Fragment() {

    private val viewModel: HistoryTagViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryTagAdapter
    private lateinit var yearTextView: TextView

    private var currentYear = Calendar.getInstance().get(Calendar.YEAR).toDouble()
    private val currentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1).toDouble()
    private var selectedYear: Double = currentYear

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_history_tag, container, false)

        recyclerView = view.findViewById(R.id.rv_month_tag)
        yearTextView = view.findViewById(R.id.tv_year)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = HistoryTagAdapter(emptyMap(), currentYear)
        recyclerView.adapter = adapter

        yearTextView.text = currentYear.toInt().toString()

        viewModel.mostTaggedData.observe(viewLifecycleOwner) { mostTagged ->
            adapter.updateData(mostTagged, currentYear)
        }

        // 년도 변경
        view.findViewById<ImageView>(R.id.ic_arrow_left).setOnClickListener {
            changeYear(-1)
        }

        view.findViewById<ImageView>(R.id.ic_arrow_right).setOnClickListener {
            changeYear(1)
        }

        view.findViewById<ImageView>(R.id.ic_history_back).setOnClickListener {
            findNavController().navigateUp()
        }

        fetchTagsForYear()

        return view
    }

    private fun changeYear(offset: Int) {
        val newYear = currentYear + offset
        val thisYear = Calendar.getInstance().get(Calendar.YEAR).toDouble()

        if (newYear in 2000.0..thisYear) { // 현재 년도 이상으로 넘어가지 않게 하기
            currentYear = newYear
            yearTextView.text = currentYear.toInt().toString()
            fetchTagsForYear()
        }
    }

    private fun fetchTagsForYear() {
        viewModel.fetchMostTaggedData(currentYear)
    }
}
