package com.umc.sweepic.presentation.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R
import com.umc.sweepic.presentation.challenge.adapter.ChallengeAdapter

class ChallengeListFragment : Fragment() {

    private lateinit var viewModel: ChallengeViewModel
    private lateinit var adapter: ChallengeAdapter
    private var challengeType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        challengeType = arguments?.getString("type") // "new" 또는 "inProgress"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel 초기화
        viewModel = ViewModelProvider(requireActivity()).get(ChallengeViewModel::class.java)

        // Adapter 초기화
        adapter = ChallengeAdapter { challenge ->
            if (challengeType == "new") {
                viewModel.moveToInProgress(challenge)
            }
        }

        // RecyclerView 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // LiveData 관찰
        if (challengeType == "new") {
            viewModel.newChallenges.observe(viewLifecycleOwner) { challenges ->
                adapter.setChallenges(challenges)
            }
        } else if (challengeType == "inProgress") {
            viewModel.inProgressChallenges.observe(viewLifecycleOwner) { challenges ->
                adapter.setChallenges(challenges)
            }
        }
    }

    companion object {
        fun newInstance(type: String): ChallengeListFragment {
            val fragment = ChallengeListFragment()
            val args = Bundle()
            args.putString("type", type)
            fragment.arguments = args
            return fragment
        }
    }
}