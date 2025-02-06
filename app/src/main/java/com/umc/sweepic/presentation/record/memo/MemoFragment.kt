package com.umc.sweepic.presentation.record.memo

import android.os.Bundle
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

        // ✅ ViewModel에서 데이터 받아오기
        viewModel.memoFolders.observe(viewLifecycleOwner) { memoFolders ->
            Log.d("MemoFragment", "memoFolders 데이터 변경 감지: ${memoFolders.size}") // ✅ 데이터 개수 확인
            adapter.updateData(memoFolders)
        }

        // ✅ API 데이터 불러오기
        viewModel.fetchMemoFolders()
    }

    private fun navigateToDetail(memoFolder: MemoFolder) {
        val action = MemoFragmentDirections.actionMemoFragmentToMemodetailFragment(memoFolder.id.toLong())
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
