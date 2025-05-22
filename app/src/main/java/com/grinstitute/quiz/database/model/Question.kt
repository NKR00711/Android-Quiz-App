package com.grinstitute.quiz.database.model

data class Question(
    val question: String,
    val answer: Long,
    val options: List<String>,
    val explanation: String
)