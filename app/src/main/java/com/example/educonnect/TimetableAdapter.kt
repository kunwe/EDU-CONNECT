package com.example.educonnect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.databinding.ItemTimetableBinding

class TimetableAdapter(private val entries: List<TimetableEntry>) :
    RecyclerView.Adapter<TimetableAdapter.VH>() {

    class VH(val binding: ItemTimetableBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTimetableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val entry = entries[position]
        holder.binding.apply {
            tvDay.text = entry.day
            tvTimeSlot.text = entry.timeSlot
            tvSubject.text = entry.subject
            tvActivity.text = "${entry.activity} (${entry.duration})"
        }
    }

    override fun getItemCount() = entries.size
}
