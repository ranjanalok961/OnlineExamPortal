package com.example.onlineexamportal

import java.util.Date

data class ExamResultList(
    val date: Date = Date(),
    val examSubject: String = "",
    val examTitle: String = "",
    val percentage: Double = 0.0,
    val professorFeedback: String = "",
    val score: Int = 0,
    val studentId: String = "",
    val totalScore: Int = 0
)