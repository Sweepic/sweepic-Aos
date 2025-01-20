package com.umc.sweepic.presentation.record.memo

import MemoAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.databinding.FragmentMemoBinding

class MemoFragment : Fragment() {
    private var _binding: FragmentMemoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MemoFolderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MemoAdapter(emptyList()) { memoFolder ->
            navigateToDetail(memoFolder)
        }

        // 리사이클러뷰 설정
        binding.rvMemoList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMemoList.adapter = adapter

        // 뷰모델에서 데이터 받아오기
        viewModel.memoFolders.observe(viewLifecycleOwner) { memoFolders ->
            adapter.updateData(memoFolders)
        }
    }

    private fun navigateToDetail(memoFolder: MemoFolder) {
        val action = MemoFragmentDirections.actionMemoFragmentToMemodetailFragment(memoFolder)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
