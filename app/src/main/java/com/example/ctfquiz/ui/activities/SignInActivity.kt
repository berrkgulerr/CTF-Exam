package com.example.ctfquiz.ui.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ctfquiz.Constants
import com.example.ctfquiz.R
import com.example.ctfquiz.RootDetection
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val rootDetection = RootDetection()
        if (rootDetection.isDeviceRooted(this)) {
            Toast.makeText(
                this, "You are rooted",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this, "You are not rooted",
                Toast.LENGTH_SHORT
            ).show()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        tv_dont_have_acc.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btn_sign_in.setOnClickListener {
            val email = et_sign_in_email.text.toString()
            val pass = et_sign_in_password.text.toString()
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, HomePageActivity::class.java)
                    intent.putExtra(Constants.USER_NAME, email)
                    intent.putExtra(Constants.currentUserid, it.result.user!!.uid)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this, "Give valid username and password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }


}