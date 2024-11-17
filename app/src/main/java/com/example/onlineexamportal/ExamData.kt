package com.example.onlineexamportal

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.Exclude
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ExamData(
    val available: Boolean = false,
    val duration: Int = 0,
    val endDate: String = "",        // Format: "yyyy-MM-dd"
    val endTime: String = "",        // Format: "HH:mm"
    val examSubject: String = "",
    val examTitle: String = "",
    val instructor: String = "",
    val professorId: String = "",
    val questions: List<MCQQuestion> = emptyList(),
    val startDate: String = "",
    val startTime: String = "",
    val whoAttempted: List<AttemptedStudent> = emptyList()
) {
    // Exclude the computed property `isAvailable` from Firestore parsing
    @get:Exclude
    val isAvailable: Boolean
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            // Combine start date and time
            val startDateTimeStr = "$startDate $startTime"
            val endDateTimeStr = "$endDate $endTime"
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            // Parse strings to LocalDateTime
            val startDateTime = LocalDateTime.parse(startDateTimeStr, formatter)
            val endDateTime = LocalDateTime.parse(endDateTimeStr, formatter)

            // Get the current date and time
            val currentDateTime = LocalDateTime.now()

            // Return true if the current time is between start and end times
            return currentDateTime.isAfter(startDateTime) && currentDateTime.isBefore(endDateTime)
        }
}

data class MCQQuestion(
    val correctAnswer: String = "",
    val marks: Int = 0,
    val options: List<String> = emptyList(),
    val question: String = ""
)

data class AttemptedStudent(
    var marks: Double = 0.0,
    var name : String = "",
    val studentId: String = ""
)
