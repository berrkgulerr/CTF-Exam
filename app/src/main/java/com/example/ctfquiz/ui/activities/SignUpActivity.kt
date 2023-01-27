package com.example.ctfquiz.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ctfquiz.R
import com.example.ctfquiz.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users")


        tv_already_registered.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_sign_up.setOnClickListener {
            val email = et_sign_up_email.text.toString()
            val pass = et_sign_up_password.text.toString()
            val confirmpass = et_sign_up_confirmPass.text.toString()

            if (validateSignUp(email, pass, confirmpass)) {
                try {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = User(it.result.user!!.email, null)
                            myRef.child(it.result.user!!.uid).setValue(user)
                            myRef.child(it.result.user!!.uid).child("isSolved")
                                .setValue(false)
                            val intent = Intent(this, SignInActivity::class.java)
                            Toast.makeText(
                                this, "Account created successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this, it.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: FirebaseAuthException) {
                    Toast.makeText(this, e.errorCode, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateSignUp(email: String, pass: String, confirmpass: String): Boolean {
        return when {
            !isValidEmail(email) -> {
                Toast.makeText(
                    this, "Give valid email",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            !isValidPassword(pass) -> {
                Toast.makeText(
                    this, "Give valid password",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            !TextUtils.equals(pass, confirmpass) -> {
                Toast.makeText(
                    this, "Passwords are not matching",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[*@#$%^&+=.])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        )
        return passwordREGEX.matcher(password).matches()
    }
}