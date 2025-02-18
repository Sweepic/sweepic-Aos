package com.umc.sweepic.presentation.record.history.award

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umc.sweepic.domain.repository.sweep.AwardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryLastBestViewModel @Inject constructor(
    private val repository: AwardRepository
) : ViewModel() { }
