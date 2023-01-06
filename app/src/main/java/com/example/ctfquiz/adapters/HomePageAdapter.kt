package com.example.ctfquiz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ctfquiz.R

class HomePageAdapter :RecyclerView.Adapter<HomePageAdapter.HomePageViewHolder>() {

    private val itemTitles = arrayOf("What is CTF?",
        "Application Deadline?",
        "What are the prizes?")
    private val itemDetails = arrayOf("\"Capture The Flag\" (CTF) competitions, in the cyber " +
            "security sense, are not related to playing outdoor running or traditional computer " +
            "games. Instead, they consist of a set of computer security puzzles, or challenges, " +
            "involving reverse-engineering, memory corruption, cryptography, web technologies, " +
            "and more.",
        "October 23th",
        "Internship opportunity in Arçelik IoT Security Team " +
                "for the top 3 and extra surprise for the winner.")
    private val itemImages = intArrayOf(
        R.drawable.what_is_ctf,
        R.drawable.deadline,
        R.drawable.prizes
    )

    inner class HomePageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var image : ImageView
        var textTitle :TextView
        var textDes : TextView

        init {
            image = itemView.findViewById(R.id.ivItemImage)
            textTitle = itemView.findViewById(R.id.tvItemHomePageTitle)
            textDes = itemView.findViewById(R.id.tvItemHomePageDesc)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_home_page,
            parent,
            false)
        return HomePageViewHolder(v)
    }

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        holder.textTitle.text = itemTitles[position]
        holder.textDes.text = itemDetails[position]
        holder.image.setImageResource(itemImages[position])
    }

    override fun getItemCount(): Int {
        return itemTitles.size
    }


}