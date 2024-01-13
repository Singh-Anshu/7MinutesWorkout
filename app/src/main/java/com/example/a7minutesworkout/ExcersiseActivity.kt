package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
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
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setUpResetView()
    }

    private fun setUpResetView(){
        binding?.flProgressBar?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.tvTitle?.text = "GET READY FOR NEXT EXERCISE"

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        setRestProgress(REST_TIMER)
    }

    private fun setUpExerciseView(){

        binding?.flProgressBar?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.tvTitle?.text = "JUMPING JACK"

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

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

                Toast.makeText(
                    this@ExcersiseActivity,
                    "Here now we will start the exercise.",
                    Toast.LENGTH_SHORT
                ).show()

                // Call the exercise again and again
                currentExercisePosition++
                exerciseProgress = 0
                setUpExerciseView()
            }

        }.start()

    }

    private fun setExerciseProgress(timer: Long){
        binding?.progressBarExercise?.progress = exerciseProgress

        restTimer = object : CountDownTimer(timer, 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = exerciseInputTime - exerciseProgress
                binding?.tvTimerExercise?.text = (exerciseInputTime - exerciseProgress).toString()
            }

            override fun onFinish() {

                Toast.makeText(
                    this@ExcersiseActivity,
                    "30 Seconds are over, lets go to the rest view.",
                    Toast.LENGTH_SHORT
                ).show()

                //Take Rest for 10 second
                setUpResetView()

            }

        }.start()

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

        binding = null
    }
}