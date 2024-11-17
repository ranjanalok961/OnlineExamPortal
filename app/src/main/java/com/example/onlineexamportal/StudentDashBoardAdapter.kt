package com.example.onlineexamportal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentDashBoardAdapter(private var exams: List<ExamData>,
                              private val onExamClicked: (ExamData) -> Unit) : RecyclerView.Adapter<StudentDashBoardAdapter.ExamViewHolder>() {

    inner class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val examTitle: TextView = itemView.findViewById(R.id.studentDashboardExamTitle)
        val examSubject: TextView = itemView.findViewById(R.id.studentDashboardExamSubject)
        val takeExamButton: Button = itemView.findViewById(R.id.takeExamButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exam_card_student, parent, false)
        return ExamViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam = exams[position]
        holder.examTitle.text = exam.examTitle
        holder.examSubject.text = exam.examSubject
        holder.takeExamButton.setOnClickListener {
            onExamClicked(exam)
        }
    }

    override fun getItemCount() = exams.size

    fun updateExams(newExams: List<ExamData>) {
        exams = newExams
        notifyDataSetChanged() // Notify the adapter of data change
    }
}
