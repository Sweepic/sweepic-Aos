package com.umc.sweepic.presentation.record.memo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.databinding.FragmentMemoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemoFragment : Fragment() {
    private var _binding: FragmentMemoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MemoFolderViewModel by activityViewModels()
    private lateinit var adapter: MemoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MemoAdapter(emptyList()) { memoFolder ->
            navigateToDetail(memoFolder)
        }

        binding.rvMemoList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMemoList.adapter = adapter

        // ViewModel에서 데이터 받아오기
        viewModel.memoFolders.observe(viewLifecycleOwner) { memoFolders ->
            adapter.updateData(memoFolders)
        }

        // API 데이터 불러오기
        viewModel.fetchMemoFolders()

        setupSearch()
    }

    private fun navigateToDetail(memoFolder: MemoFolder) {
        val action = MemoFragmentDirections.actionMemoFragmentToMemodetailFragment(memoFolder.id.toLong())
        findNavController().navigate(action)
    }

    private fun setupSearch() {
        binding.btnSearch.setOnClickListener {
            val keyword = binding.etSearch.text.toString().trim()
            if (keyword.isNotEmpty()) {
                viewModel.searchMemoFolders(keyword)
            } else {
                viewModel.fetchMemoFolders()
            }
        }

        //검색창 내 글자 사라지면 다시 원래 폴더 목록 불러오도록 하기
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            private var previousText: String = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                previousText = s.toString() // 변경 전 텍스트 저장
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentText = s.toString().trim()

                if (currentText.length < previousText.length || currentText.isEmpty()) {
                    viewModel.fetchMemoFolders()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
