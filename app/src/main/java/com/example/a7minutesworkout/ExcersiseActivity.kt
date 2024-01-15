package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import com.example.a7minutesworkout.databinding.ActivityExcersiseBinding
import java.util.Objects

class ExcersiseActivity : AppCompatActivity() {

    private var binding: ActivityExcersiseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var inputTime = 10
    private var exerciseInputTime = 30

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcersiseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        exerciseList = Constants.defaultExerciseList()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showConfirmationDialog(
                    this@ExcersiseActivity,
                    R.string.are_you_sure_you_want_to_cancel,
                    R.drawable.ic_cancel_order
                ) {
                    finish()

                }
            }
        })

        binding?.toolbarExercise?.setNavigationOnClickListener {
            showConfirmationDialog(
                this@ExcersiseActivity,
                R.string.are_you_sure_you_want_to_cancel,
                R.drawable.ic_cancel_order
            ) {
                finish()

            }
        }

        binding?.tvTimerExercise?.setOnClickListener {

        }

        setUpResetView()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }

    private fun setUpResetView(){

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvUpComingExerciseLabel?.visibility = View.VISIBLE
        binding?.tvUpComingExerciseValue?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.exerciseImg?.visibility = View.GONE

        if(restTimer != null){
            restTimer?.cancel()
            restTimer = null
            restProgress = 0
        }
        binding?.tvUpComingExerciseValue?.text = exerciseList!![currentExercisePosition+1].getName()


        setRestProgress(REST_TIMER)
    }

    private fun setUpExerciseView(){

        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvUpComingExerciseLabel?.visibility = View.INVISIBLE
        binding?.tvUpComingExerciseValue?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.exerciseImg?.visibility = View.VISIBLE

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseTimer = null
            exerciseProgress = 0
        }

        binding?.exerciseImg?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()

        setExerciseProgress(EXERCISE_TIMER)
    }


    private fun setRestProgress(timer: Long){
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(timer, 1000) {
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress = inputTime - restProgress
                binding?.tvTimer?.text = (inputTime - restProgress).toString()

            }

            override fun onFinish() {

                // Call the exercise again and again
                currentExercisePosition++
                exerciseProgress = 0
                setUpExerciseView()
            }

        }.start()


    }

    private fun setExerciseProgress(timer: Long){
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(timer, 1000) {
            override fun onTick(p0: Long) {


                exerciseProgress++
                binding?.progressBarExercise?.progress = exerciseInputTime - exerciseProgress
                binding?.tvTimerExercise?.text =
                    (exerciseInputTime - exerciseProgress).toString()

            }

            override fun onFinish() {

                // TODO(Step 10 - Updating the view after completing the 30 seconds exercise.)
                // START
                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    setUpResetView()
                } else {

                    Toast.makeText(
                        this@ExcersiseActivity,
                        "Congratulations! You have completed the 7 minutes workout.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // END

            }

        }.start()

    }

    override fun onResume() {
        super.onResume()

        if(restTimer != null){
            restTimer?.start()
        }

        if(exerciseTimer != null){
            exerciseTimer?.start()

        }


        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }

    override fun onPause() {
        super.onPause()

        if(restTimer != null){
            restTimer?.cancel()
        }

        if(exerciseTimer != null){
            exerciseTimer?.cancel()

        }

        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = null
    }
}