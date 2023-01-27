package com.example.ctfquiz.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ctfquiz.R
import com.example.ctfquiz.adapters.HomePageAdapter
import kotlinx.android.synthetic.main.nav_home_page.*

class HomePageFragment : Fragment(R.layout.nav_home_page) {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var homePageAdapter: RecyclerView.Adapter<HomePageAdapter.HomePageViewHolder>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(context)
        rvHomePage.layoutManager = layoutManager

        homePageAdapter = HomePageAdapter()
        rvHomePage.adapter = homePageAdapter
    }

}