package com.grinstitute.quiz.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val cid: Long = 0L,
    val question: String = "",
    val options: ArrayList<String> = arrayListOf(),
    val answer: Long = 0L,
    val explanation: String = ""
) : Parcelable