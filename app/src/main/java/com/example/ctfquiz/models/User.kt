package com.example.ctfquiz.models

data class User(
    val email: String? = "",
    var answers: ArrayList<Int>? = ArrayList()
) {
}