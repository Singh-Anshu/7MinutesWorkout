package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityTextToSpeechAcctivityBinding
import java.util.Locale

class TextToSpeechAcctivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityTextToSpeechAcctivityBinding ? = null

    private var tts : TextToSpeech? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextToSpeechAcctivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        tts = TextToSpeech(this, this)

        binding?.textToSpeechButtom?.setOnClickListener{
            if (binding?.textToSpeechValue?.text.toString().isEmpty()) {
                Toast.makeText(
                    this@TextToSpeechAcctivity,
                    "Enter a text to speak.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                speakOut(binding?.textToSpeechValue?.text.toString())
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()

        if(tts != null){
            tts?.stop()
            tts?.shutdown()
        }

        binding = null
    }
}