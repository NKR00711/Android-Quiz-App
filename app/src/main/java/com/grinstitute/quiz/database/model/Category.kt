package com.grinstitute.quiz.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category (
    var id: Long = 0,
    var name: String = "",
    var image: String = ""
): Parcelable