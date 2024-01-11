package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityExcersiseBinding
import java.util.Objects

class ExcersiseActivity : AppCompatActivity() {

    private var binding: ActivityExcersiseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var inputTime = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcersiseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setUpResetView(10000)
    }

    private fun setUpResetView(timer: Long){

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        setRestProgress(timer)
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

                binding?.tvTitle?.text = "Exercise 1"
                inputTime = 30
                binding?.progressBar?.max = 30
                setUpResetView(30000)
            }

        }.start()

    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        binding = null
    }
}