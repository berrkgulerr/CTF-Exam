package com.example.ctfquiz.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ctfquiz.R
import kotlinx.android.synthetic.main.nav_home.*

class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_home)

        bottomNavigationView.setupWithNavController(ctfNavHostFragment.findNavController())

    }
}