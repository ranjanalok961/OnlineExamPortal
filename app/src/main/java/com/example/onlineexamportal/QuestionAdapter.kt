package com.example.onlineexamportal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuestionAdapter(
    private val questions: MutableList<MCQQuestion>
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTextView: TextView = itemView.findViewById(R.id.questionTextView)
        val option1TextView: TextView = itemView.findViewById(R.id.option1TextView)
        val option2TextView: TextView = itemView.findViewById(R.id.option2TextView)
        val option3TextView: TextView = itemView.findViewById(R.id.option3TextView)
        val option4TextView: TextView = itemView.findViewById(R.id.option4TextView)
        val correctAnswerTextView: TextView = itemView.findViewById(R.id.correctAnswerTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]

        // Set the question text
        holder.questionTextView.text = question.question

        // Set options
        holder.option1TextView.text = question.options.getOrNull(0) ?: "Option 1 not set"
        holder.option2TextView.text = question.options.getOrNull(1) ?: "Option 2 not set"
        holder.option3TextView.text = question.options.getOrNull(2) ?: "Option 3 not set"
        holder.option4TextView.text = question.options.getOrNull(3) ?: "Option 4 not set"

        // Set the correct answer
        holder.correctAnswerTextView.text = "Correct Answer: ${question.correctAnswer}"


    }

    override fun getItemCount(): Int = questions.size
}
