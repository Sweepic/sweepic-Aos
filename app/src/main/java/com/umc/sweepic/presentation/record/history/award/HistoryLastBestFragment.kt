package com.umc.sweepic.presentation.record.history.award

import HistoryLastBestAdapter
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.databinding.FragmentHistoryLastBestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryLastBestFragment : Fragment() {

    private var _binding: FragmentHistoryLastBestBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryLastBestViewModel by viewModels()
    private lateinit var adapter: HistoryLastBestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryLastBestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()
        observeViewModel()

        viewModel.getAwards() // API 호출
    }

    private fun setupRecyclerView() {
        adapter = HistoryLastBestAdapter()
        binding.rvLastBestPhotos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLastBestPhotos.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.awards.observe(viewLifecycleOwner, Observer { awards ->
            adapter.submitList(awards) // RecyclerView 업데이트
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
