package com.example.onlineexamportal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.util.Date

class Student_Quiz : AppCompatActivity() {
    private lateinit var examTitle: TextView
    private lateinit var instructions: TextView
    private lateinit var question: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var optionA: RadioButton
    private lateinit var optionB: RadioButton
    private lateinit var optionC: RadioButton
    private lateinit var optionD: RadioButton
    private lateinit var previousButton: Button
    private lateinit var nextButton: Button
    private lateinit var submitExamButton: Button

    private lateinit var examData: ExamData
    private var currentQuestionIndex = 0
    private lateinit var selectedAnswers: MutableList<Int>
    private lateinit var examResult: ExamResultList

    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    private lateinit var name : String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_quiz)

        setDataToExamData()

        examTitle = findViewById(R.id.examTitle)
        instructions = findViewById(R.id.instructions)
        question = findViewById(R.id.question)
        optionsGroup = findViewById(R.id.optionsGroup)
        optionA = findViewById(R.id.optionA)
        optionB = findViewById(R.id.optionB)
        optionC = findViewById(R.id.optionC)
        optionD = findViewById(R.id.optionD)
        previousButton = findViewById(R.id.previousButton)
        nextButton = findViewById(R.id.nextButton)
        submitExamButton = findViewById(R.id.submitExamButton)
        selectedAnswers = MutableList(examData.questions.size) { -1 }

        updateQuestion()

        // RadioGroup listener to capture the selected option
        optionsGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedOptionIndex = when (checkedId) {
                R.id.optionA -> 0
                R.id.optionB -> 1
                R.id.optionC -> 2
                R.id.optionD -> 3
                else -> -1
            }
            if (selectedOptionIndex != -1) {
                selectedAnswers[currentQuestionIndex] = selectedOptionIndex
            }
        }

        // Set click listeners
        previousButton.setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                updateQuestion()
            } else {
                Toast.makeText(this, "This is the first question.", Toast.LENGTH_SHORT).show()
            }
        }

        nextButton.setOnClickListener {
            if (currentQuestionIndex <  examData.questions.size - 1) {
                currentQuestionIndex++
                updateQuestion()
            } else {
                Toast.makeText(this, "This is the last question.", Toast.LENGTH_SHORT).show()
            }
        }

        submitExamButton.setOnClickListener {
            submitExam()
        }
    }
    private fun setDataToExamData(){
        val examDataJson = intent.getStringExtra("EXAM_DATA")
        if (examDataJson != null) {
            // Convert JSON string back to ExamData object
            val gson = Gson()
            examData = gson.fromJson(examDataJson, ExamData::class.java)
            Log.d("Data2", examData.toString())
//            findViewById<TextView>(R.id.quiz).text = examData.examTitle
        } else {
            Log.d("Data2", "Exam data is null")
        }
    }
    private fun updateQuestion() {
        // Update question text and number
        question.text = examData.questions[currentQuestionIndex].question
        optionA.text = examData.questions[currentQuestionIndex].options[0]
        optionB.text = examData.questions[currentQuestionIndex].options[1]
        optionC.text = examData.questions[currentQuestionIndex].options[2]
        optionD.text = examData.questions[currentQuestionIndex].options[3]

        // Clear previous selection
        optionsGroup.clearCheck()
    }

    private fun submitExam() {
        var score = 0
        val totalScore = examData.questions.sumOf { it.marks }

        for (i in 0 until examData.questions.size) {
            val selectedOptionIndex = selectedAnswers[i]

            if (selectedOptionIndex != -1) {
                if (examData.questions[i].correctAnswer == examData.questions[i].options[selectedOptionIndex]) {
                    score += examData.questions[i].marks
                }
            }
        }
        Toast.makeText(this, "Exam submitted successfully! Your score: $score", Toast.LENGTH_SHORT).show()

        val percentage = (score.toDouble() / totalScore) * 100

        examResult = firebaseAuth.currentUser?.let {
            ExamResultList(
                date = Date(),
                examSubject = examData.examSubject,
                examTitle = examData.examTitle,
                percentage = percentage,
                professorFeedback = generateFeedback(percentage),
                score = score,
                studentId = it.uid,
                totalScore = totalScore,

                )
        }!!

        val db = FirebaseFirestore.getInstance()
        val resultCollection = db.collection("result")

        // Check if a result for this exam and student already exists
        if (examResult != null) {
            resultCollection
                .whereEqualTo("examTitle", examResult.examTitle)
                .whereEqualTo("studentId", examResult.studentId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        // Update existing document
                        val existingDocId = querySnapshot.documents[0].id
                        resultCollection.document(existingDocId)
                            .set(examResult) // Using .set() to overwrite existing document
                            .addOnSuccessListener {
                                Toast.makeText(this, "Exam updated successfully! Your score: $score", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to update exam: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // Create new document if no previous result exists
                        resultCollection
                            .add(examResult)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Exam submitted successfully! Your score: $score", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to submit exam: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error checking existing results: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

//      update data on fistore
        val id = FirebaseAuth.getInstance().currentUser?.uid
        if (id != null) {
            db.collection("user").document(id)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if(documentSnapshot.exists()){
                        val user = documentSnapshot.getString("name")
                        if (user != null) {
                            name = user
                        }
                    }
                }
        }
        val professorId = examData.professorId
        val examcollection  = db.collection("exams")
        if(examResult != null && id != null){
            examcollection
                .whereEqualTo("professorId",professorId)
                .get()
                .addOnSuccessListener {query ->
                    if(!query.isEmpty){
                        val existId = query.documents[0].id
                        Log.d("examId",existId)
                        val exam =  examcollection.document(existId)

                        exam.get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val examData = documentSnapshot.toObject(ExamData::class.java)

                                    // Check if the student ID already exists in the whoAttempted list
                                    val updatedList = examData?.whoAttempted?.toMutableList() ?: mutableListOf()
                                    val existingStudent = updatedList.find { it.studentId == id }

                                    if (existingStudent != null) {
                                        // Update the existing student's marks
                                        existingStudent.marks = score.toDouble()
                                    } else {
                                        // Add new student to the list
                                        updatedList.add(AttemptedStudent(score.toDouble(),name,id))
                                    }

                                    // Update the document in Firestore
                                    exam.update("whoAttempted", updatedList)
                                        .addOnSuccessListener {
                                            println("whoAttempted list successfully updated!")
                                        }
                                        .addOnFailureListener { e ->
                                            println("Error updating whoAttempted list: $e")
                                        }
                                } else {
                                    println("Document does not exist!")
                                }
                            }
                    }
                }
        }
//        start new Activity
        val gson = Gson()
        val examResultData = gson.toJson(examResult)
        val intent = Intent(this,ExamSubmissionConfirmation::class.java)
        intent.putExtra("ExamResult",examResultData)
        startActivity(intent)
    }
    private fun generateFeedback(percentage: Double): String {
        return when {
            percentage >= 90 -> "Excellent work! You have a strong grasp of the material."
            percentage >= 80 -> "Great job! You performed very well."
            percentage >= 70 -> "Good effort! Keep practicing to improve further."
            percentage >= 60 -> "You passed, but there's room for improvement."
            percentage >= 50 -> "You just made it. Consider reviewing the material to strengthen your understanding."
            percentage >= 40 -> "Below average. It's recommended to go over the topics again."
            percentage >= 30 -> "You struggled with this exam. Focus on studying the material."
            percentage >= 20 -> "Poor performance. Try to put more time into understanding the subject."
            percentage >= 10 -> "Very low score. Consider seeking extra help with the material."
            else -> "Failed. Significant improvement is needed."
        }
    }


}