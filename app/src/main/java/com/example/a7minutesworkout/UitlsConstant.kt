package com.example.a7minutesworkout

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.a7minutesworkout.databinding.DialogMessageConfirmationBinding

const val  REST_TIMER: Long = 10000
const val EXERCISE_TIMER : Long= 30000

var currentGlobalDialog: AlertDialog? = null
/**
 * message in resource
 */
fun showConfirmationDialog(cxt: Context?, message: Int, icon: Int, function: () -> Unit) {

    cxt?.let { context ->
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        val binding: DialogMessageConfirmationBinding =
            DialogMessageConfirmationBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog = builder.create()
        currentGlobalDialog = dialog
        dialog.window?.setBackgroundDrawableResource(R.drawable.round_corner_dialog)
        binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(context, icon))
        binding.tvMessage.text = context.getString(message)
        binding.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
        binding.buttonOk.setOnClickListener {
            dialog.dismiss()
            function()
        }
        dialog.show()
    }
}