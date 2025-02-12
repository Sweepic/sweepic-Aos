package com.umc.sweepic.presentation.record.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R
import com.umc.sweepic.presentation.record.Adapter.HistoryTagAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryTagFragment : Fragment() {

    private val viewModel: HistoryTagViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryTagAdapter
    private var currentYear: Double = 2024.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_history_tag, container, false)

        recyclerView = view.findViewById(R.id.rv_month_tag)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = HistoryTagAdapter(emptyMap())
        recyclerView.adapter = adapter

        // 화살표 클릭 시 연도 바뀜!
        view.findViewById<ImageView>(R.id.ic_arrow_left).setOnClickListener {
            changeYear(-1)
        }

        view.findViewById<ImageView>(R.id.ic_arrow_right).setOnClickListener {
            changeYear(1)
        }

        view.findViewById<ImageView>(R.id.ic_history_back).setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.tagsByMonth.observe(viewLifecycleOwner) { tagsByMonth ->
            adapter.updateData(tagsByMonth)
        }

        fetchTagsForYear()

        return view
    }

    private fun changeYear(offset: Int) {
        currentYear += offset
        fetchTagsForYear()
    }

    private fun fetchTagsForYear() {
        viewModel.fetchTagsByYear(currentYear)
    }
}
