package com.grinstitute.quiz.database.model

data class QuizResult(
    val total: Int,
    val correct: Int,
    val wrong: Int,
    val unattempted: Int
)
