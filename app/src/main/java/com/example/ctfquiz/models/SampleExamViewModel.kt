package com.example.ctfquiz.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctfquiz.repository.SampleExamRepository

class SampleExamViewModel : ViewModel()  {
    private val repository : SampleExamRepository = SampleExamRepository().getInstance()
    private val _allSampleExams = MutableLiveData<List<SampleExam>>()
    val allSampleExams : LiveData<List<SampleExam>> = _allSampleExams

    init {
        repository.loadSampleExams(_allSampleExams)
    }
}