package com.example.onlineexamportal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class ExamResultDetails : AppCompatActivity() {

    private lateinit var resultData: ExamResultList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exam_result_details)

        getData()
        setLayout()
    }

    private fun getData() {
        val resultDataJson = intent.getStringExtra("ExamResult")
        if (resultDataJson != null) {
            // Convert JSON string back to ExamResultList object
            val gson = Gson()
            resultData = gson.fromJson(resultDataJson, ExamResultList::class.java)
            Log.d("Data", resultData.toString())
        } else {
            Log.d("Data", "Exam result data is null")
        }
    }

    private fun setLayout() {
        // Initialize Views
        val examResultsTitle = findViewById<TextView>(R.id.examResultsTitle)
        val examNameDetail = findViewById<TextView>(R.id.examNameDetail)
        val examDate = findViewById<TextView>(R.id.examDate)
        val totalScore = findViewById<TextView>(R.id.totalScore)
        val mcqScore = findViewById<TextView>(R.id.mcqScore)
        val subjectiveScore = findViewById<TextView>(R.id.subjectiveScore)
        val professorFeedback = findViewById<TextView>(R.id.professorFeedback)
        val downloadPdfButton = findViewById<Button>(R.id.downloadPdfButton)

        // Set Data to Views
        examResultsTitle.text = "Exam Results" // static text, could be made dynamic
        examNameDetail.text = resultData.examTitle
        examDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(resultData.date)
        totalScore.text = "${resultData.score}/${resultData.totalScore}"

        // Set MCQ and Subjective Scores if they are part of the resultData model
        // Here assumed they are part of the total score
        mcqScore.text = "${resultData.score}/${resultData.totalScore / 2}" // Assuming half total for MCQ
        subjectiveScore.text = "${resultData.percentage}" // Assuming other half for subjective

        professorFeedback.text = resultData.professorFeedback

        // Set Download PDF button functionality
        downloadPdfButton.setOnClickListener {
            // Placeholder for PDF download logic
            Toast.makeText(this, "Download PDF functionality to be implemented", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,StudentHome::class.java))
        }
    }
}
