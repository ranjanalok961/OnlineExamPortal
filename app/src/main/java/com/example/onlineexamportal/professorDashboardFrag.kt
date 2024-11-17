package com.example.onlineexamportal

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.time.LocalDate

class professorDashboardFrag : Fragment() {
    private lateinit var createNewExamButton: Button
    private lateinit var examsRecyclerView: RecyclerView
    private lateinit var examAdapter: ExamAdapter
    private val examList = mutableListOf<ExamData>()
    private val filteredExamList = mutableListOf<ExamData>()

    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_professor_dashboard, container, false)

        // Set up the button to create a new exam
        createNewExamButton = view.findViewById(R.id.createExamButton)
        createNewExamButton.setOnClickListener {
            startActivity(Intent(requireActivity(), createNewExam::class.java))
        }

        // Set up the RecyclerView
        examsRecyclerView = view.findViewById(R.id.examsRecyclerView)
        examsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        examAdapter = ExamAdapter(filteredExamList) { selectedExam ->
            val gson = Gson()
            val exam = gson.toJson(selectedExam)
            val intent = Intent(requireActivity(), ProfessorExamDetails::class.java)
            intent.putExtra("ExamResult", exam)
            startActivity(intent)
        }
        examsRecyclerView.adapter = examAdapter

        // Set up the category buttons
        val allButton: Button = view.findViewById(R.id.allButton)
        val upComingButton: Button = view.findViewById(R.id.upComingButton)
        val activeButton: Button = view.findViewById(R.id.activeButton)
        val completeButton: Button = view.findViewById(R.id.completeButton)

        allButton.setOnClickListener {
            filterExams("All")
        }
        upComingButton.setOnClickListener {
            filterExams("Upcoming")
        }
        activeButton.setOnClickListener {
            filterExams("Active")
        }
        completeButton.setOnClickListener {
            filterExams("Complete")
        }

        // Fetch exams from Firestore
        fetchExams()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchExams() {
        val uid = firebaseAuth.currentUser?.uid
        uid?.let {
            firestore.collection("exams")
                .whereEqualTo("professorId", uid)
                .get()
                .addOnSuccessListener { result ->
                    examList.clear()
                    for (document in result) {
                        try {
                            val examData = document.toObject(ExamData::class.java)
                            examList.add(examData)
                            Log.d("Firestore", "Exam fetched: $examData")
                        } catch (e: Exception) {
                            Log.e("Firestore", "Error parsing exam data: ${e.message}")
                        }
                    }
                    // Initially show all exams
                    filterExams("All")
                }
                .addOnFailureListener { exception ->
                    Log.w("Firestore", "Error getting exams: ", exception)
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterExams(filterType: String) {
        filteredExamList.clear()
        for (exam in examList) {
            when (filterType) {
                "All" -> filteredExamList.add(exam) // Show all exams
                "Upcoming" -> {
                    if (exam.startDate > getCurrentDate()) {
                        filteredExamList.add(exam) // Show upcoming exams based on start date
                    }
                }
                "Active" -> {
                    if (exam.isAvailable) {
                        filteredExamList.add(exam) // Show active exams
                    }
                }
                "Complete" -> {
                    if (!exam.isAvailable) {
                        filteredExamList.add(exam) // Show completed exams (not available)
                    }
                }
            }
        }
        examAdapter.notifyDataSetChanged()
    }

    // A helper function to get the current date in the same format as exams
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val current = LocalDate.now()
        return current.toString() // Format is yyyy-MM-dd
    }
}
