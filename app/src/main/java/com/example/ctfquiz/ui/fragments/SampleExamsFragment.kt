package com.example.ctfquiz.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ctfquiz.R
import com.example.ctfquiz.adapters.SampleExamAdapter
import com.example.ctfquiz.models.SampleExam
import com.example.ctfquiz.models.SampleExamViewModel
import com.example.ctfquiz.ui.activities.QuizQuestionActivity

class SampleExamsFragment : Fragment(R.layout.nav_sample_exams) {
    lateinit var viewModel: SampleExamViewModel
    lateinit var sampleExamRecyclerView: RecyclerView
    lateinit var sampleExamAdapter: SampleExamAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sampleExamRecyclerView = view.findViewById(R.id.rvSampleExams)
        sampleExamRecyclerView.layoutManager = LinearLayoutManager(context)
        sampleExamRecyclerView.setHasFixedSize(true)
        sampleExamAdapter = SampleExamAdapter()
        sampleExamRecyclerView.adapter = sampleExamAdapter
        viewModel = ViewModelProvider(this)[SampleExamViewModel::class.java]
        viewModel.allSampleExams.observe(viewLifecycleOwner) {
            sampleExamAdapter.differ.submitList(it)
        }
        sampleExamAdapter.setOnItemClickListener(object : SampleExamAdapter.OnItemClickListener{
            override fun onItemClick(sampleExam: SampleExam) {
                val intent = Intent(context, QuizQuestionActivity::class.java)
                intent.putExtra("examId",sampleExam.id)
                startActivity(intent)
            }
        })
    }


}