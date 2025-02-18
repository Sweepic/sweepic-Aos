package com.umc.sweepic.presentation.record.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R
import com.umc.sweepic.presentation.record.Adapter.HistoryTagAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HistoryTagFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryTagAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_tag, container, false)

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.rv_month_tag)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // "월"을 바꾸기 위한 더미 데이터 생성
        val exampleData = List(12) { "${12-it}월" } // 1월~12월 더미 데이터

        recyclerView.addItemDecoration(VerticalSpaceItemDecoration(16))

        // 어댑터 설정
        adapter = HistoryTagAdapter(exampleData)
        recyclerView.adapter = adapter

        //뒤로 가기 누르면 메인 히스토리 페이지로 이동
        val backButton = view.findViewById<ImageView>(R.id.ic_history_back)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
