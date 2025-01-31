package com.umc.sweepic.presentation.record.TagBord

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentDetailImgBinding

class DetailImgFragment : Fragment(R.layout.fragment_detail_img) {
    private var _binding: FragmentDetailImgBinding? = null
    private val binding get() = _binding!!

    private var date: String? = null
    private var images: List<String> = emptyList()
    private var tags: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailImgBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 데이터 가져오기
        date = arguments?.getString("date")
        Log.d("DetailImgFragment", "Received date: $date")
        images = arguments?.getStringArrayList("images") ?: emptyList()
        tags = arguments?.getStringArrayList("tags") ?: emptyList()

        // 날짜 표시
        binding.tvDate.text = date
        // 태그 RecyclerView 설정
        binding.rcChip.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcChip.adapter = ChipAdapter(tags ?: emptyList())

        // ViewPager2 어댑터 설정 (이미지 슬라이드)
        binding.viewPager.adapter = DetailImgPagerAdapter(images ?: emptyList())


        // RecyclerView 설정
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
