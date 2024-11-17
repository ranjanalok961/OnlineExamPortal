package com.example.onlineexamportal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class StudentDashboard : Fragment() {
    private val exams = mutableListOf<ExamData>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recycleAdapter: StudentDashBoardAdapter

    private val firestoreAuth by lazy { FirebaseAuth.getInstance() }
    private val firestoreDb by lazy { FirebaseFirestore.getInstance() }

    private val takeExamViewModel: QuesionViewModel by activityViewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_dashboard, container, false)
        CallFireStore()
        recyclerView = view.findViewById(R.id.studentExamRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recycleAdapter = StudentDashBoardAdapter(exams) { examData ->
            takeExamViewModel.setTakeExamData(examData)

            val gson = Gson()
            val examDataJson = gson.toJson(examData)

            Log.d("ExamData", examData.examTitle)
            val intent = Intent(requireActivity(), StudentExamInstruction::class.java)
            intent.putExtra("EXAM_DATA", examDataJson)
            startActivity(intent)
        }
        recyclerView.adapter = recycleAdapter


        // Search bar functionality
        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                filterExams(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val filterIcon = view.findViewById<ImageView>(R.id.filterIcon)
        val filterSpinner = view.findViewById<Spinner>(R.id.filterSpinner)
        filterIcon.setOnClickListener {
            filterSpinner.visibility = if (filterSpinner.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun CallFireStore() {
        firestoreDb.collection("exams")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    try {
                        val examData = document.toObject(ExamData::class.java)
                        exams.add(examData)
                        Log.d("Firestore", "Exam fetched: $examData")
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error parsing exam data: ${e.message}")
                    }
                }
                recycleAdapter.notifyDataSetChanged() // Notify adapter of data changes
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting exams: ", exception)
            }
    }
    private fun filterExams(query: String) {
        val filteredExams = exams.filter {
            // Check if the exam title contains the search query (case-insensitive)
            it.examTitle.contains(query, ignoreCase = true) ||
                    it.examSubject.contains(query , ignoreCase = true)
        }

        // Update the adapter with filtered data
        recycleAdapter.updateExams(filteredExams)
    }
}
