package com.example.ctfquiz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ctfquiz.R
import com.example.ctfquiz.models.SampleExam
import kotlinx.android.synthetic.main.item_sample_exam.view.*

class SampleExamAdapter : RecyclerView.Adapter<SampleExamAdapter.SampleExamViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(sampleExam: SampleExam)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    inner class SampleExamViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(differ.currentList[adapterPosition])
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<SampleExam>() {
        override fun areItemsTheSame(oldItem: SampleExam, newItem: SampleExam): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SampleExam, newItem: SampleExam): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleExamViewHolder {
        return SampleExamViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_sample_exam,
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: SampleExamViewHolder, position: Int) {
        val sampleExam = differ.currentList[position]
        holder.itemView.apply {
            tv_sample_exam_name.text = sampleExam.sampleExamName
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}