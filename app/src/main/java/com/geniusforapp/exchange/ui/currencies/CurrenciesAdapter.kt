package com.geniusforapp.exchange.ui.currencies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.geniusforapp.exchange.BuildConfig
import com.geniusforapp.exchange.R
import com.geniusforapp.exchange.domain.entities.Rate
import com.geniusforapp.exchange.domain.entities.formatCurrency
import kotlinx.android.synthetic.main.item_currency.view.*

class CurrenciesAdapter : ListAdapter<Rate, CurrenciesViewHolder>(CurrenciesDiffCallBack()) {

    lateinit var onRateClicked: (Rate) -> Unit


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesViewHolder {
        return parent.inflate(R.layout.item_currency)
    }

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition), onRateClicked)
    }
}

private fun ViewGroup.inflate(itemCurrency: Int): CurrenciesViewHolder {
    return CurrenciesViewHolder(LayoutInflater.from(context).inflate(itemCurrency, this, false))
}

class CurrenciesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Rate, onRateClicked: (Rate) -> Unit) {
        itemView.imageBase.load(String.format(BuildConfig.FLAG_URL, item.name.substring(0, 2)))
        itemView.textRate.text = String.format(
            "1 %s = %s",
            item.base,
            item.price.formatCurrency(item.name)
        )

        itemView.setOnClickListener { onRateClicked(item) }
    }
}

class CurrenciesDiffCallBack : DiffUtil.ItemCallback<Rate>() {
    override fun areItemsTheSame(oldItem: Rate, newItem: Rate): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Rate, newItem: Rate): Boolean {
        return oldItem.name == newItem.name
    }
}