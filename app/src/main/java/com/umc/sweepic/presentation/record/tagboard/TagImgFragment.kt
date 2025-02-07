package com.umc.sweepic.presentation.record.tagboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentTagImgBinding

class TagImgFragment : Fragment(R.layout.fragment_tag_img) {

    private var _binding: FragmentTagImgBinding? = null
    private val binding get() = _binding!!

    private var selectedTag: String? = null
    private lateinit var tagImgAdapter: TagImgAdapter
    private var filteredImages: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTagImgBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ 전달받은 태그 정보 가져오기
        selectedTag = arguments?.getString("selectedTag")
        filteredImages = arguments?.getStringArrayList("filteredImages") ?: emptyList()

        // ✅ 디버깅 로그 추가
        Log.d("TagImgFragment", "Selected tag: $selectedTag")
        Log.d("TagImgFragment", "Filtered images count: ${filteredImages.size}")

        binding.tvTitle.text = selectedTag ?: "태그 이미지"

        // ✅ 리사이클러뷰 설정
        binding.rcImg.layoutManager = GridLayoutManager(requireContext(), 2)
        tagImgAdapter = TagImgAdapter(filteredImages)
        binding.rcImg.adapter = tagImgAdapter

        // ✅ 뒤로가기 버튼 설정
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

