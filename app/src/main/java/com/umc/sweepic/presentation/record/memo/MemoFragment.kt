package com.umc.sweepic.presentation.record

import MemoAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentMemoBinding
import com.umc.sweepic.presentation.record.memo.MemoFolder
import com.umc.sweepic.presentation.record.memo.MemoFolderViewModel

class MemoFragment : Fragment(R.layout.fragment_memo) {

    private var _binding: FragmentMemoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MemoFolderViewModel by viewModels()
    private lateinit var memoAdapter: MemoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMemoBinding.bind(view)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        memoAdapter = MemoAdapter(emptyList()) { memoFolder ->
            navigateToDetailFragment(memoFolder)
        }

        binding.rvMemoList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = memoAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.memoFolders.observe(viewLifecycleOwner) { memoFolders ->
            memoAdapter.updateData(memoFolders)
        }
    }

    private fun navigateToDetailFragment(memoFolder: MemoFolder) {
        val navController = requireParentFragment().findNavController() // 부모 프래그먼트의 NavController 사용
        val action = RecordFragmentDirections.actionRecordFragmentToMemoDetailFragment(memoFolder)
        navController.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
