package com.example.ctfquiz.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.ctfquiz.Constants
import com.example.ctfquiz.R
import com.example.ctfquiz.ui.activities.CtfExamActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.nav_ctf_exam.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


import java.time.Duration
import java.util.Timer
import java.util.TimerTask

class CtfExamFragment : Fragment(R.layout.nav_ctf_exam) {

    // Add variables for the timer and the updateCounter task
    private var timer: Timer? = null
    private var updateCounter: Runnable? = null
    private var isSolved: Boolean = false
    private var curUserID: String = ""

    private lateinit var database: FirebaseDatabase
    private lateinit var ctfExamDateTimeRef: DatabaseReference
    private lateinit var isSolvedRef: DatabaseReference
    private lateinit var ctfExamDatabaseRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        ctfExamDatabaseRef = database.getReference("/CTF_Exam_1")
        ctfExamDateTimeRef = database.getReference("/CTF_Exam_1/dateTime")
        curUserID = firebaseAuth.currentUser!!.uid
        isSolvedRef = database.getReference("/users/${curUserID}/isSolved")
        isSolvedRef.get().addOnSuccessListener {
            isSolved = it.value as Boolean
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Attach value event listener
        ctfExamDateTimeRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dateTimeString = dataSnapshot.getValue(String::class.java)
                val pattern = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                val targetDateTime = LocalDateTime.parse(dateTimeString, pattern)

                // Check if the target date and time has passed
                if (targetDateTime.isBefore(LocalDateTime.now())) {
                    // The target date and time has passed
                    tv_ctf_exam_will_start.visibility = View.GONE
                    btn_start_ctf_exam.visibility = View.VISIBLE
                    btn_start_ctf_exam.setOnClickListener {
                        if (isSolved) {
                            Toast.makeText(
                                context, "You have already solved this exam. " +
                                        "Results will be announced on 30th January!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            isSolved = true
                            isSolvedRef.setValue(true)
                            val intent = Intent(context, CtfExamActivity::class.java)
                            intent.putExtra(Constants.currentUserid, curUserID)
                            startActivity(intent)
                        }
                    }
                } else {
                    // The target date and time has not yet passed
                    tv_ctf_exam_will_start.visibility = View.VISIBLE
                    btn_start_ctf_exam.visibility = View.GONE

                    // Update the counter display in your app
                    updateCounter = Runnable {
                        val currentTime = LocalDateTime.now()
                        val duration = Duration.between(currentTime, targetDateTime)

                        // Calculate the days, hours, minutes, and seconds left
                        val days = duration.toDays()
                        val hours = duration.toHours() % 24
                        val minutes = duration.toMinutes() % 60
                        val seconds = duration.seconds % 60

                        // Check if the duration has reached 0
                        if (days <= 0 && hours <= 0 && minutes <= 0 && seconds <= 0) {
                            // Show the button and cancel the timer
                            activity?.runOnUiThread {
                                tv_ctf_exam_will_start.visibility = View.GONE
                                btn_start_ctf_exam.visibility = View.VISIBLE
                                btn_start_ctf_exam.setOnClickListener {
                                    if (isSolved) {
                                        Toast.makeText(
                                            context, "You have already solved " +
                                                    "this exam. Results will be announced on 30th " +
                                                    "January!", Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        isSolved = true
                                        isSolvedRef.setValue(true)
                                        val intent = Intent(context, CtfExamActivity::class.java)
                                        intent.putExtra(Constants.currentUserid, curUserID)
                                        startActivity(intent)
                                    }
                                }
                            }
                            timer?.cancel()
                        } else {
                            // Update the counter display
                            activity?.runOnUiThread {
                                tv_ctf_exam_will_start.text = "Time left: $days d " +
                                        "$hours h $minutes m $seconds s"
                            }
                        }
                    }
                    // Start the timer
                    timer = Timer()
                    timer?.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            updateCounter?.run()
                        }
                    }, 0, 1000) // Update the counter every 1 second
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                // Handle the error
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
