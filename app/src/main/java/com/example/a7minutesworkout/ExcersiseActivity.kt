package com.example.a7minutesworkout

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityExcersiseBinding
import com.example.a7minutesworkout.db.ExerciseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Locale

class ExcersiseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityExcersiseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var inputTime = 10
    private var exerciseInputTime = 30

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var isPausedRest = false
    private var isPausedExercise = false

    private var tts : TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseStatusAdapter:ExerciseStatusAdapter? = null

    lateinit var database : ExerciseDatabase ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcersiseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        tts = TextToSpeech(this, this)

        // Initalizing Room DB
        database?.getAppDatabase(applicationContext)


        // Getting list of all exercise from Constants class
        exerciseList = Constants.defaultExerciseList()
        // inserting the data in Room Database
        insertExerciseData()
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
            if(!isPausedExercise) {
                exerciseTimer?.cancel()
                isPausedExercise = true
            }else{
                isPausedExercise = false
                exerciseTimer?.start()
            }
        }

        binding?.tvTimer?.setOnClickListener {
            if(!isPausedRest) {
                restTimer?.cancel()
                isPausedRest = true
            }else{
                isPausedRest= false
                restTimer?.start()
            }
        }

        setUpResetView()
        setUpExerciseStatusAdapter()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }
    private fun insertExerciseData(exerciseList: List<ExerciseModel>) {
        // Use CoroutineScope to launch a coroutine for suspend function
        lifecycleScope.launch(Dispatchers.IO) {
            database?.exerciseDao()?.insertExerciseData(exerciseList)
        }
    }


    private fun setUpExerciseStatusAdapter(){
        exerciseStatusAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.apply {
            layoutManager = LinearLayoutManager(this@ExcersiseActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = exerciseStatusAdapter
        }
    }

    private fun setUpResetView(){

        try {
            val soundURI =
                Uri.parse("android.resource://com.example.a7minutesworkout/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false // Sets the player to be looping or non-looping.
            player?.start() // Starts Playback.

        }catch (e: Exception){
            e.printStackTrace()
            Log.e("MediaPlayer Exception", e.message.toString())
        }

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvUpComingExerciseLabel?.visibility = View.VISIBLE
        binding?.tvUpComingExerciseValue?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.exerciseImg?.visibility = View.GONE

        if (restTimer != null) {
            restTimer?.cancel()
            restTimer = null
            restProgress = 0
        }
        speakOut("Wait for 10 Second Next exercise is "+exerciseList!![currentExercisePosition+1].getName())
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

        speakOut(exerciseList!![currentExercisePosition].getName())

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
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseStatusAdapter?.notifyDataSetChanged()
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
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseStatusAdapter?.notifyDataSetChanged()
                    setUpResetView()
                } else {

                    finish()
                    val intent = Intent(this@ExcersiseActivity, FinishActivity::class.java)
                    startActivity(intent)
                    /*Toast.makeText(
                        this@ExcersiseActivity,
                        "Congratulations! You have completed the 7 minutes workout.",
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
                // END

            }

        }.start()

    }

    override fun onResume() {
        super.onResume()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }

    override fun onPause() {
        super.onPause()

        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }

    override fun onDestroy() {
        super.onDestroy()

        if(tts != null){
            tts?.stop()
            tts?.shutdown()
        }

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

    override fun onInit(status: Int) {

        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The Language is not supported")
            }
        }else{
            Log.e("TTS", "Initializing is Failed!")
        }
    }

    private fun speakOut(text: String){
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}