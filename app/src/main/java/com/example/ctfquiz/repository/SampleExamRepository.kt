package com.example.ctfquiz.repository

import androidx.lifecycle.MutableLiveData
import com.example.ctfquiz.models.SampleExam
import com.google.firebase.database.*

class SampleExamRepository {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("sampleExams")
    @Volatile
    private var sampleExamRepositoryInstance: SampleExamRepository? = null

    fun getInstance(): SampleExamRepository {
        return sampleExamRepositoryInstance ?: synchronized(this) {

            val instance = SampleExamRepository()
            sampleExamRepositoryInstance = instance
            instance
        }
    }

    fun loadSampleExams(SampleExamList: MutableLiveData<List<SampleExam>>) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val sExamList: List<SampleExam> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(SampleExam::class.java)!!
                    }
                    SampleExamList.postValue(sExamList)

                } catch (_: Exception) {
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}