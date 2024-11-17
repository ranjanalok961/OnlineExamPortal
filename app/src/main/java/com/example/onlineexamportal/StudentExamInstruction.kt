package com.example.onlineexamportal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class StudentExamInstruction : AppCompatActivity() {
    private val viewModel: QuesionViewModel by viewModels()
    private lateinit var examData: ExamData
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_exam_instruction)

        val examDataJson = intent.getStringExtra("EXAM_DATA")
        if (examDataJson != null) {
            // Convert JSON string back to ExamData object
            val gson = Gson()
            examData = gson.fromJson(examDataJson, ExamData::class.java)
            Log.d("Data2", examData.toString())
            findViewById<TextView>(R.id.instructionExamTitle).text = "Exam Title: ${examData.examTitle}"
        } else {
            Log.d("Data2", "Exam data is null")
        }

        findViewById<Button>(R.id.startExamButton).setOnClickListener {
            val gson = Gson()
            val examDataJson2 = gson.toJson(examData)
            val intent = Intent(this, Student_Quiz::class.java)
            intent.putExtra("EXAM_DATA", examDataJson2)
            startActivity(intent)
        }
    }


}
