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

class NewChallengeFragment : Fragment() {

    private lateinit var viewModel: ChallengeViewModel
    private lateinit var adapter: ChallengeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_challenge_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ChallengeViewModel::class.java)

        adapter = ChallengeAdapter { challenge ->
            viewModel.moveToInProgress(challenge)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewModel.newChallenges.observe(viewLifecycleOwner) { challenges ->
            adapter.setChallenges(challenges)
        }
    }
}
