package com.example.educonnect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.databinding.ItemSummaryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SummaryHistoryAdapter(
    private val onSummaryClick: (Summary) -> Unit
) : ListAdapter<Summary, SummaryHistoryAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Summary>() {
            override fun areItemsTheSame(a: Summary, b: Summary) = a.summaryId == b.summaryId
            override fun areContentsTheSame(a: Summary, b: Summary) = a == b
        }
    }

    inner class VH(val binding: ItemSummaryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val summary = getItem(position)
        holder.binding.apply {
            tvSummaryTitle.text = summary.title
            tvSummaryMeta.text = "${summary.subject} • Grade ${summary.grade}"
            tvSummaryPreview.text = summary.summaryText
            
            val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(summary.createdAt))
            tvSummaryDate.text = date

            root.setOnClickListener { onSummaryClick(summary) }
        }
    }
}
