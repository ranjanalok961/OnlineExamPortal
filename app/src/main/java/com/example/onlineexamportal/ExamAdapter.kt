package com.example.onlineexamportal

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class ExamAdapter(
    private val exams: List<ExamData>,
    private val onItemClickListener: (ExamData) -> Unit
) : RecyclerView.Adapter<ExamAdapter.ExamViewHolder>() {

    class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val examTitleTextView: TextView = itemView.findViewById(R.id.examTitleTextView)
        val examSubjectTextView: TextView = itemView.findViewById(R.id.examSubjectTextView)
        val examDatesTextView: TextView = itemView.findViewById(R.id.examDatesTextView)
        val examStatusTextView: TextView = itemView.findViewById(R.id.examStatusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exam_card, parent, false)
        return ExamViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam = exams[position]
        holder.examTitleTextView.text = exam.examTitle
        holder.examSubjectTextView.text = exam.examSubject
        holder.examDatesTextView.text = "From: ${exam.startDate} ${exam.startTime} to ${exam.endDate} ${exam.endTime}"

        // Display exam status
        holder.examStatusTextView.text = if (exam.isAvailable) "Available" else "Not Available"
        holder.examStatusTextView.setTextColor(if (exam.isAvailable) android.graphics.Color.GREEN else android.graphics.Color.RED)

        holder.itemView.setOnClickListener {
            onItemClickListener(exam)
        }

    }

    override fun getItemCount(): Int = exams.size
}
