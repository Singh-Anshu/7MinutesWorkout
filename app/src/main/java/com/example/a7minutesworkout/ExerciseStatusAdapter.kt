package com.example.a7minutesworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(var itemList: List<ExerciseModel>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ExerciseStatusViewHolder(binding: ItemExerciseStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val b: ItemExerciseStatusBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExerciseStatusViewHolder(
            ItemExerciseStatusBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return itemList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ExerciseStatusViewHolder) {

            // val model :ExerciseModel = itemList[position]
            itemList?.get(position)?.let { model ->

                holder.b.tvItem.text = model.id.toString()

//            holder.b.tvItem.text = model.getId().toString()

                when {
                    model.isSelected2 -> {
                        holder.b.tvItem.background = ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.item_circular_color_white_background
                        )
                        holder.b.tvItem.setTextColor(Color.parseColor("#212121"))
                    }

                    model.isCompleted2 -> {
                        holder.b.tvItem.background = ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.item_circular_color_accent_background
                        )
                        holder.b.tvItem.setTextColor(Color.parseColor("#FFFFFF"))
                    }

                    else -> {
                        holder.b.tvItem.background = ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.item_circular_color_gray_background
                        )
                        holder.b.tvItem.setTextColor(Color.parseColor("#212121"))
                    }
                }

            }
        }
    }
}