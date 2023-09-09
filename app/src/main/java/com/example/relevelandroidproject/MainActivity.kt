package com.example.relevelandroidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.relevelandroidproject.databinding.ActivityMainBinding
import com.example.relevelandroidproject.domain.model.Coin
import com.example.relevelandroidproject.presentation.Coin.CoinViewModel
import com.example.relevelandroidproject.presentation.CoinList.CoinAdapter
import com.example.relevelandroidproject.presentation.CoinListViewModel
import com.example.relevelandroidproject.util.ResponseState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var coinAdapter: CoinAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var page: Int = 1
    private val tempCoinList = arrayListOf<Coin>()
    private val coinListViewModel: CoinListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layoutManager = GridLayoutManager(this, 2)
        setUpTheRecyclerView()
        binding.btSort.setOnClickListener {
            tempCoinList.sortWith { o1, o2 -> o1.name.compareTo(o2.name) }
            coinAdapter.setData(tempCoinList)
        }
        callAPI()

        binding.coinRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1) {
                    page += 1
                    coinListViewModel.getAllCoins(page.toString())
                    callAPI()
                }
            }
        })

    }

    private fun callAPI() {
        CoroutineScope(Dispatchers.IO).launch {
            coinListViewModel.getAllCoins(page.toString())
            coinListViewModel._coinListValue.collectLatest { coinListValue ->

                withContext(Dispatchers.Main) {
                    if (coinListValue.isLoading) {

                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        if (coinListValue.error.isNotBlank()) {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@MainActivity,
                                coinListValue.error,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            binding.progressBar.visibility = View.GONE
                            tempCoinList.addAll(coinListValue.coinsList)
                            coinAdapter.setData(tempCoinList as ArrayList<Coin>)
                        }
                    }
                }
            }
        }
    }

    private fun setUpTheRecyclerView() {
        coinAdapter = CoinAdapter(this@MainActivity, ArrayList())
        binding.coinRecyclerView.adapter = coinAdapter
        binding.coinRecyclerView.layoutManager = layoutManager
        binding.coinRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.coinRecyclerView.context,
                (GridLayoutManager(this, 1)).orientation
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val search = menu?.findItem(R.id.menuSearch)
        val searchView = search?.actionView as androidx.appcompat.widget.SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText?.isEmpty()!!) {
            coinAdapter.setData(tempCoinList)
        } else {
            coinAdapter.filter.filter(newText)
        }
        return true
    }

}