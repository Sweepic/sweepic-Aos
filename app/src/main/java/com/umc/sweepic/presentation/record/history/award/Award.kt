package com.umc.sweepic.domain.model

import android.net.Uri

data class Award(
    val id: Int,
    val date: String,
    val photoUris: List<Uri>
)
