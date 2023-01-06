package com.example.ctfquiz.ui.activities
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.ctfquiz.Constants
import com.example.ctfquiz.models.Question
import com.example.ctfquiz.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_quiz_question.*

class CtfExamActivity : AppCompatActivity(), OnClickListener {

    private var mCurrentPosition : Int = 1
    private var mSelectedOptionPosition : Int = 0
    private var options = ArrayList<TextView>()
    private var isSubmitButtonClicked: Int = 0
    private var mUserName: String? = ""
    private var curUserID: String? = ""

    private var id: Int = 0
    private var question: String = ""
    private var image: String = ""
    private var optionOne: String = ""
    private var optionTwo: String = ""
    private var optionThree: String = ""
    private var optionFour: String = ""
    private var correctAnswer: Int = 0
    private var answerList : ArrayList<Int> = ArrayList()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.reference
        val myRef = firebaseDatabase.getReference("/users")
        val questionsFromDatabase = databaseReference.child("/CTF_Exam_1/questions")

        questionsFromDatabase.get().addOnSuccessListener {
            mUserName = intent.getStringExtra(Constants.USER_NAME)
            curUserID = intent.getStringExtra(Constants.currentUserid)
            options.add(0,tv_option_one)
            options.add(1,tv_option_two)
            options.add(2,tv_option_three)
            options.add(3,tv_option_four)
            val mQuestionsList = ArrayList<Question>()
            for (questionChild in it.children){
                for (child in questionChild.children){
                    when(child.key){
                        "id"->id = (child.value as Number).toInt()
                        "question"->question = child.value as String
                        "img"->image = child.value as String
                        "optionOne"->optionOne = child.value as String
                        "optionTwo"->optionTwo = child.value as String
                        "optionThree"->optionThree = child.value as String
                        "optionFour"->optionFour = child.value as String
                        "correctAns"->correctAnswer = (child.value as Number).toInt()
                    }
                }
                val que = Question(id,question, image, optionOne, optionTwo,
                    optionThree, optionFour, correctAnswer)
                mQuestionsList.add(que)
                setQuestion(mQuestionsList)
                tv_option_one.setOnClickListener {
                    if (isSubmitButtonClicked==0)
                        selectedOptionView(tv_option_one, 1)
                }
                tv_option_two.setOnClickListener{
                    if (isSubmitButtonClicked==0)
                        selectedOptionView(tv_option_two, 2)
                }
                tv_option_three.setOnClickListener{
                    if (isSubmitButtonClicked==0)
                        selectedOptionView(tv_option_three, 3)
                }
                tv_option_four.setOnClickListener{
                    if (isSubmitButtonClicked==0)
                        selectedOptionView(tv_option_four, 4)
                }
                btn_submit.setOnClickListener{
                    if(isSubmitButtonClicked==1){
                        // If not last question load next question.
                        if(mCurrentPosition<mQuestionsList.size){
                            mCurrentPosition++
                            isSubmitButtonClicked=0
                            mSelectedOptionPosition=0
                            setQuestion(mQuestionsList)
                        }
                        else{
                            // If last question load result activity.
                            Toast.makeText(this, "You have successfully finished " +
                                    "the exam and submitted your answers. Results will be " +
                                    "announced on 30th January!", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, HomePageActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    else{
                        if(mSelectedOptionPosition==0) {
                            Toast.makeText(this,
                                "Please choose one of the options",
                                Toast.LENGTH_SHORT).show()
                        }
                        else{
                            isSubmitButtonClicked = 1
                            answerList.add(mSelectedOptionPosition)
                            if (mCurrentPosition == mQuestionsList.size) {
                                btn_submit.text = "FINISH"
                                myRef.child(curUserID!!).child("answers")
                                    .setValue(answerList.toList())
                            } else {
                                btn_submit.text = "GO TO NEXT QUESTION"
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion(mQuestionsList : ArrayList<Question>){
        val question = mQuestionsList[mCurrentPosition-1]
        defaultOptionsView()

        if(mCurrentPosition == mQuestionsList.size){
            btn_submit.text = "SUBMIT & FINISH"
        }else{
            btn_submit.text = "SUBMIT"
        }

        progressBar.progress = mCurrentPosition
        progressBar.max = mQuestionsList.size
        tv_progress.text = "$mCurrentPosition/${mQuestionsList.size}"
        tv_question.text = question.question

        Glide.with(this).load(question.image).into(iv_image)

        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    private fun defaultOptionsView(){
        id = 0
        question = ""
        image = ""
        optionOne = ""
        optionTwo = ""
        optionThree = ""
        optionFour = ""
        correctAnswer = 0

        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg

            )
        }
    }

    private fun selectedOptionView(tv:TextView, selectedOptionNum:Int){
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}