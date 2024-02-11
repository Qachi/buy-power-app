package com.example.buypowerapp.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.buypowerapp.R
import com.example.buypowerapp.databinding.ActivityOrderHistoryBinding

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBarColor()
        supportActionBar()
    }

    private fun statusBarColor() {
        window.statusBarColor = ContextCompat.getColor(
            this,
            R.color.holo_orange_dark
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun supportActionBar() {
        val toolbar = binding.buyUnitToolbar.buyUnitToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.orderHistory_transaction)
    }
}