package com.umc.sweepic.presentation.challenge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umc.sweepic.R
import com.umc.sweepic.domain.model.Challenge

class ChallengeViewModel : ViewModel() {

    private val _newChallenges = MutableLiveData<List<Challenge>>()
    val newChallenges: LiveData<List<Challenge>> get() = _newChallenges

    private val _inProgressChallenges = MutableLiveData<List<Challenge>>()
    val inProgressChallenges: LiveData<List<Challenge>> get() = _inProgressChallenges

    init {
        loadChallenges()
    }

    private fun loadChallenges() {
        _newChallenges.value = listOf(
            Challenge("92장", "스크린캡처 앨범 정리", R.drawable.img_test),
            Challenge("100장", "몽골에서의 추억 엄선하기", R.drawable.img_test)
        )
        _inProgressChallenges.value = emptyList()
    }


    fun moveToInProgress(challenge: Challenge) {
        val currentNewChallenges = _newChallenges.value?.toMutableList() ?: mutableListOf()
        val currentInProgressChallenges = _inProgressChallenges.value?.toMutableList() ?: mutableListOf()

        if (currentNewChallenges.remove(challenge)) {
            currentInProgressChallenges.add(challenge)
        }

        _newChallenges.value = currentNewChallenges
        _inProgressChallenges.value = currentInProgressChallenges
    }
}
