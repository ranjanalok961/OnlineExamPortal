package com.example.onlineexamportal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuesionViewModel : ViewModel() {
    private val _takeExamData = MutableLiveData<ExamData>()
    val takeExamData: LiveData<ExamData> get() = _takeExamData

    fun setTakeExamData(examData: ExamData) {
        _takeExamData.value = examData
        Log.d("QuesionViewModel", "Exam data set: ${_takeExamData.value}")
    }

    fun refreshTakeExamData() {
        // Notify observers without changing the value
        _takeExamData.value = _takeExamData.value
    }
}
