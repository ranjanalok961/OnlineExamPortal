package com.example.onlineexamportal

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class ProfessorExamDetails : AppCompatActivity() {
    private lateinit var exam: ExamData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_professor_exam_details)
        getData()
        val recycle = findViewById<RecyclerView>(R.id.recycleListOfStudent)
        recycle.layoutManager = LinearLayoutManager(this)
        recycle.adapter = professorResultOverViewApater(exam.whoAttempted)
    }
    private fun getData(){
        val resultDataJson = intent.getStringExtra("ExamResult")
        if (resultDataJson != null) {
            // Convert JSON string back to ExamData object
            val gson = Gson()
            exam = gson.fromJson(resultDataJson, ExamData::class.java)
            Log.d("Data2", exam.whoAttempted[0].studentId)
        } else {
            Log.d("Data2", "Exam data is null")
        }
    }
}