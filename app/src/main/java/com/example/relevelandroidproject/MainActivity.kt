package com.example.relevelandroidproject

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.relevelandroidproject.domain.model.Coin
import com.example.relevelandroidproject.presentation.CoinList.CoinAdapter
import com.example.relevelandroidproject.presentation.CoinListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private var valueRepeat = 3
    private lateinit var coinRecyclerView   : RecyclerView
    private lateinit var coinAdapter        : CoinAdapter
    private lateinit var progressBar        : ProgressBar
    private lateinit var search             : String
    private lateinit var layoutManager : GridLayoutManager
    private lateinit var sort : Button
    private val tempCoinList = arrayListOf<Coin>()
    private var page = 1
    private val coinListViewModel : CoinListViewModel by viewModels()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sort = findViewById(R.id.btSort)
        progressBar         = findViewById(R.id.progressBar)
        coinRecyclerView   = findViewById(R.id.coinRecyclerView)
        layoutManager = GridLayoutManager(this,2)
        recyclerView()
        sort.setOnClickListener{
            tempCoinList.sortWith { o1, o2 -> o1!!.name.compareTo(o2!!.name) }
            coinAdapter.setData(tempCoinList)
        }
        coinRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(layoutManager.findLastVisibleItemPosition()==layoutManager.itemCount-1)
                {
                    page += 1
                    coinListViewModel.getAllCoins(page.toString())
                    callAPI()
                }
            }
        })
    }
    private fun callAPI(){
        CoroutineScope(Dispatchers.Main).launch {
            repeat(valueRepeat){
                coinListViewModel._coinListValue.collect{value->
                    when {
                        value.isLoading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        value.error.isNotBlank() -> {
                            progressBar.visibility = View.GONE
                            valueRepeat = 0
                            Toast.makeText(this@MainActivity, value.error, Toast.LENGTH_LONG).show()
                        }
                        value.coinsList.isNotEmpty() -> {
                            progressBar.visibility = View.GONE
                            valueRepeat = 0
                            tempCoinList.addAll(value.coinsList)
                            coinAdapter.setData(tempCoinList as ArrayList<Coin>)
                        }
                    }
                    delay(1000)
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val search = menu?.findItem(R.id.menuSearch)
        val searchView = search?.actionView as androidx.appcompat.widget.SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(queryText: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText?.isEmpty()!!){
            coinAdapter.setData(tempCoinList)
        }
        else{
            coinAdapter.filter.filter(newText)
        }
        return true
    }

    private fun recyclerView(){
        coinAdapter = CoinAdapter(this@MainActivity,ArrayList())
        coinRecyclerView.adapter = coinAdapter
        coinRecyclerView.layoutManager = layoutManager
        coinRecyclerView.addItemDecoration(DividerItemDecoration(coinRecyclerView.context,(GridLayoutManager(this,1)).orientation))
    }

    override fun onStart() {
        super.onStart()
        coinListViewModel.getAllCoins(page.toString())
        callAPI()
    }
}