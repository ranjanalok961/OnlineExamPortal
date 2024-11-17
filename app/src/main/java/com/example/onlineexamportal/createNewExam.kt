package com.example.onlineexamportal

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class createNewExam : AppCompatActivity() {

    private lateinit var startDateEditText : EditText
    private lateinit var startTimeEditText : EditText
    private lateinit var endDateEditText : EditText
    private lateinit var endTimeEditText : EditText

    private val mcqQuestions = mutableListOf<MCQQuestion>()
    private lateinit var questionRecyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter

    private val firestoreAuth by lazy { FirebaseAuth.getInstance() }
    private val firestoreDb by lazy { FirebaseFirestore.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_new_exam)

        questionRecyclerView = findViewById(R.id.questionRecyclerView)
        questionRecyclerView.layoutManager = LinearLayoutManager(this)
        questionAdapter = QuestionAdapter(mcqQuestions)
        questionRecyclerView.adapter = questionAdapter

        findViewById<Button>(R.id.addMCQButton).setOnClickListener {
            showAddMCQDialog()
        }
        findViewById<Button>(R.id.saveAndPublishButton).setOnClickListener {
            setupButtons()
        }

        manageDateTime()

    }

    // Function to show date picker dialog
    @SuppressLint("DefaultLocale")
    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Format the selected date to "yyyy-MM-dd"
            val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            onDateSelected(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    // Function to show time picker dialog
    @SuppressLint("DefaultLocale")
    private fun showTimePickerDialog(onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            // Format the selected time to "HH:mm"
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(selectedTime)
        }, hour, minute, true)

        timePickerDialog.show()
    }
    private fun manageDateTime(){
        startDateEditText = findViewById<TextInputEditText>(R.id.startDateEditText)
        startTimeEditText = findViewById<TextInputEditText>(R.id.startTimeEditText)
        endDateEditText = findViewById<TextInputEditText>(R.id.endDateEditText)
        endTimeEditText = findViewById<TextInputEditText>(R.id.endTimeEditText)
        startDateEditText.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                startDateEditText.setText(selectedDate)
            }
        }

        startTimeEditText.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                startTimeEditText.setText(selectedTime)
            }
        }

        endDateEditText.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                endDateEditText.setText(selectedDate)
            }
        }

        endTimeEditText.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                endTimeEditText.setText(selectedTime)
            }
        }
    }


    private fun setupButtons() {
        // Initialize views

        val examTitleEditText = findViewById<TextInputEditText>(R.id.examTitleEditText)
        val subjectEditText = findViewById<TextInputEditText>(R.id.subjectEditText)
        val instructionEditText = findViewById<TextInputEditText>(R.id.instructionsEditText)
        val durationEditText = findViewById<TextInputEditText>(R.id.examDurationEditText)

        val examTitle = examTitleEditText.text.toString()
        val examSubject = subjectEditText.text.toString()
        val instruction = instructionEditText.text.toString()
        val duration = durationEditText.text.toString().toIntOrNull() ?: 0
        val startDate = startDateEditText.text.toString()
        val startTime = startTimeEditText.text.toString()
        val endDate = endDateEditText.text.toString()
        val endTime = endTimeEditText.text.toString()

        if (examTitle.isBlank() || examSubject.isBlank() || duration == 0 || startDate.isBlank() || startTime.isBlank() || endDate.isBlank() || endTime.isBlank()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            val proid = firestoreAuth.currentUser?.uid
            val examData = ExamData(
                available = true,
                duration = duration,
                endDate = endDate,
                endTime = endTime,
                examSubject = examSubject,
                examTitle = examTitle,
                instructor = instruction, // Add the instructor's name here
                professorId = proid!!,  // Add the professor ID here
                questions = mcqQuestions,
                startDate = startDate,
                startTime = startTime,
                whoAttempted = emptyList()
            )

            firestoreDb.collection("exams")
                .add(examData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Exam saved successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save exam: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
        startActivity(Intent(this,ProfessorHome::class.java))
    }
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    private fun showAddMCQDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_mcq, null)
        val mcqQuestionEditText: EditText = dialogView.findViewById(R.id.mcqQuestionEditText)
        val option1EditText: EditText = dialogView.findViewById(R.id.option1EditText)
        val option2EditText: EditText = dialogView.findViewById(R.id.option2EditText)
        val option3EditText: EditText = dialogView.findViewById(R.id.option3EditText)
        val option4EditText: EditText = dialogView.findViewById(R.id.option4EditText)
        val correctAnswerEditText: EditText = dialogView.findViewById(R.id.correctAnswerEditText)
        val marksEditText : EditText = dialogView.findViewById(R.id.marks)

        AlertDialog.Builder(this)
            .setTitle("Add MCQ")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val question = mcqQuestionEditText.text.toString()
                val options = listOf(
                    option1EditText.text.toString(),
                    option2EditText.text.toString(),
                    option3EditText.text.toString(),
                    option4EditText.text.toString(),
                )
                val correctAnswer = correctAnswerEditText.text.toString()
                val marks = marksEditText.text.toString().toIntOrNull() ?:100


                if (mcqQuestions.size == 1) {
                    questionRecyclerView.visibility = View.VISIBLE
                }

                mcqQuestions.add(MCQQuestion(correctAnswer,marks,options,question))
                Log.d("AddQuestion","$mcqQuestions")
                questionAdapter.notifyDataSetChanged()

            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
