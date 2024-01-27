package com.dicoding.asclepius.adapter

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.dicoding.asclepius.helper.HistoryDiffCallback
import com.dicoding.asclepius.view.detailHistory.DetailHistoryActivity
import com.dicoding.asclepius.view.detailHistory.DetailHistoryActivity.Companion.DATE
import com.dicoding.asclepius.view.detailHistory.DetailHistoryActivity.Companion.HIGH_SCORE_LABEL
import com.dicoding.asclepius.view.detailHistory.DetailHistoryActivity.Companion.HIGH_SCORE_VALUE
import com.dicoding.asclepius.view.detailHistory.DetailHistoryActivity.Companion.IMG_SRC
import com.dicoding.asclepius.view.detailHistory.DetailHistoryActivity.Companion.LOW_SCORE_LABEL
import com.dicoding.asclepius.view.detailHistory.DetailHistoryActivity.Companion.LOW_SCORE_VALUE
import com.dicoding.asclepius.view.history.HistoryViewModel

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val listHistory = ArrayList<History>()
    private lateinit var historyViewModel: HistoryViewModel

    fun setListHistory(listHistory: List<History>) {
        val diffCallback = HistoryDiffCallback(this.listHistory, listHistory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listHistory.clear()
        this.listHistory.addAll(listHistory)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setHistoryViewModel(historyViewModel: HistoryViewModel) {
        this.historyViewModel = historyViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = listHistory[position]
        val resultStringFormat = holder.itemView.context.getString(R.string.analysis_history_result)
        holder.bind(history, resultStringFormat)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailHistoryActivity::class.java)
            intent.putExtra(IMG_SRC, history.imgSrc)
            intent.putExtra(HIGH_SCORE_LABEL, history.highScoreLabel)
            intent.putExtra(HIGH_SCORE_VALUE, history.highScoreValue.toString())
            intent.putExtra(LOW_SCORE_LABEL, history.lowScoreLabel)
            intent.putExtra(LOW_SCORE_VALUE, history.lowScoreValue.toString())
            intent.putExtra(DATE, history.date)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listHistory.size

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History, resultStringFormat: String) {
            val imgUri = Uri.parse(history.imgSrc)
            with(binding) {
                ivImage.setImageURI(imgUri)
                tvResult.text = String.format(
                    resultStringFormat,
                    history.highScoreLabel,
                    history.highScoreValue
                )
                tvDate.text = history.date
                btnDelete.setOnClickListener {
                    historyViewModel.deleteById(history.id)
                }
            }
        }
    }
}