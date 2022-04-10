package com.example.relevelandroidproject.presentation.CoinList

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.relevelandroidproject.R
import com.example.relevelandroidproject.domain.model.Coin
import com.example.relevelandroidproject.presentation.Coin.CoinActivity
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class CoinAdapter(private val context: Context, var coinsList : ArrayList<Coin>)
    :RecyclerView.Adapter<CoinAdapter.CoinViewHolder>(),Filterable {
    lateinit var filteredList : ArrayList<Coin>
    inner class CoinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coinLayout: LinearLayout = view.findViewById(R.id.coinLinearLayout)
        val coinImage: ImageView = view.findViewById(R.id.imgCoinImage)
        val coinName: TextView = view.findViewById(R.id.txtCoinName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val coinView =
            LayoutInflater.from(parent.context).inflate(R.layout.coin_recycler_view, parent, false)
        return CoinViewHolder(coinView)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val list = coinsList[position]
        holder.coinName.text = list.name
        Picasso.get().load(list.image).into(holder.coinImage)
        holder.coinLayout.setOnClickListener {
            val intent = Intent(context, CoinActivity::class.java)
            intent.putExtra("id", list.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return coinsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<Coin>) {
        this.filteredList = list
        this.coinsList = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val string = constraint?.toString() ?: ""
                if (string.isNotEmpty()) {
                    Log.d("coinsFirst",coinsList.size.toString())
                    Log.d("coinsSecond",filteredList.size.toString())
                    var arrayList = arrayListOf<Coin>()
                    filteredList.filter {
                        it.name.lowercase().contains(string.lowercase())
                    }.forEach{
                        arrayList.add(it)
                    }
                    filteredList.clear()
                    filteredList.addAll(arrayList)
                }
                else{
                    filteredList = coinsList
                }
                return FilterResults().apply {
                    this.values = filteredList
                }
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                (if (results?.values == null)
                    ArrayList<Coin>()
                else {
                    setData(filteredList)
                })
            }
        }
    }
}