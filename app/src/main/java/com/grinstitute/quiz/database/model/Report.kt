package com.grinstitute.quiz.database.model

data class Report(
    val userName: String = "",
    val userEmail: String = "",
    val cid: Long = 0,
    val questionNumber: Int = 0,
    val issueType: String = "",
    val reason: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
