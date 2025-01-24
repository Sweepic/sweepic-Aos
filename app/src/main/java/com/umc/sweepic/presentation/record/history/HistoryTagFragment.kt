package com.umc.sweepic.presentation.record.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R
import com.umc.sweepic.presentation.record.Adapter.HistoryTagAdapter




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

        return view
    }
}
