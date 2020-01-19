package com.jhm.android.firebasecrud

import com.google.firebase.Timestamp

data class PostData(
    val title: String? = null,
    val content: String? = null,
    val time: Timestamp? = null,
    val id: String? = null
)