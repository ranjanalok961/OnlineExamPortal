package com.example.onlineexamportal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson

class ExamSubmissionConfirmation : AppCompatActivity() {
    private lateinit var resultData: ExamResultList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exam_submission_confirmation)
        getData()
        findViewById<TextView>(R.id.percentageText).text = resultData.percentage.toString()
        findViewById<Button>(R.id.viewSubmissionDetailsButton).setOnClickListener {
            val gson = Gson()
            val examResultData = gson.toJson(resultData)
            val intent = Intent(this, ExamResultDetails::class.java)
            intent.putExtra("ExamResult", examResultData)
            startActivity(intent)
        }
        findViewById<Button>(R.id.returnToDashboardButton).setOnClickListener {
            startActivity(Intent(this,StudentHome::class.java))
        }
        findViewById<Button>(R.id.retakeExamButton).setOnClickListener {
            startActivity(Intent(this,StudentExamInstruction::class.java))
        }
    }
    private fun getData(){
        val resultDataJson = intent.getStringExtra("ExamResult")
        if (resultDataJson != null) {
            // Convert JSON string back to ExamData object
            val gson = Gson()
            resultData = gson.fromJson(resultDataJson, ExamResultList::class.java)
            Log.d("Data2", resultData.toString())
//            findViewById<TextView>(R.id.quiz).text = examData.examTitle
        } else {
            Log.d("Data2", "Exam data is null")
        }
    }
}