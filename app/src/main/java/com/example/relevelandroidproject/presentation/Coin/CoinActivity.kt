package com.example.relevelandroidproject.presentation.Coin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.relevelandroidproject.R
import com.example.relevelandroidproject.databinding.ActivityCoinBinding
import com.example.relevelandroidproject.databinding.ActivityMainBinding
import com.example.relevelandroidproject.domain.model.Coin
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoinActivity : AppCompatActivity() {
    private var valueRepeat = 3
    private lateinit var binding : ActivityCoinBinding
    private val viewModel : CoinViewModel by viewModels()
    private var id:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent!=null){
            val id = intent.getStringExtra("id")
            Log.d("insideIntent",id.toString())
            viewModel.getCoinById("tether")
            callCoinApi(id.toString())
        }
    }
    private fun callCoinApi(id:String){
        CoroutineScope(Dispatchers.Main).launch {
            repeat(valueRepeat){
                Log.d("id",id)
                viewModel._coinValue.collect{value->
                    Log.d("id",id)
                    when {
                        value.isLoading -> {
                            binding.coinProgressBar.visibility = View.VISIBLE
                            Log.d("idloading",id)
                        }
                        value.error.isNotBlank() -> {
                            binding.coinProgressBar.visibility = View.GONE
                            Log.d("iderror",id)
                            valueRepeat = 0
                        }
                        value.coinDetail!=null->{
                                Log.d("id",id)
                                Log.d("coindetail",value.coinDetail.name)
                                binding.coinProgressBar.visibility = View.GONE
                                valueRepeat = 0
                                Picasso.get().load(value.coinDetail.image).into(binding.imgCoinDetail)
                                binding.txtCoinNameDetail.text = value.coinDetail.name
                                binding.txtCoinPrice.text = value.coinDetail.price.toString()
                                binding.txtCoinPriceLow.text = value.coinDetail.lowPrice.toString()
                                binding.txtCoinPriceHigh.text = value.coinDetail.highPrice.toString()
                                binding.txtCoinMarketCap.text = value.coinDetail.price_percent_change.toString()
                                binding.txtCoinPricePercentChange.text = value.coinDetail.price_percent_change.toString()
                            }
                        }
                    }
                    delay(1000)
                }
            }
        }
    override fun onStart() {
        super.onStart()
        if(id.isBlank()){
            if(intent!=null){
                id = intent.getStringExtra("id").toString()
                viewModel.getCoinById(id)
                callCoinApi(id)
            }
        }
    }
}
