package com.example.buypowerapp.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.buypowerapp.R
import com.example.buypowerapp.databinding.ActivityBuyUnitBinding

class BuyUnitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyUnitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyUnitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBarColor()
        buyPowerText()
        supportActionbar()
        arrayListOfStates()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private fun statusBarColor() {
        window.statusBarColor = ContextCompat.getColor(
            this,
            R.color.holo_orange_dark
        )
    }

    private fun buyPowerText() {
        val htmlString = getString(R.string.buy_power_html_string)
        val coloredHtmlString = String.format(
            htmlString,
            getColor(R.color.golden_yellow),
            getColor(R.color.holo_green_light)
        )
        val spannedText = HtmlCompat.fromHtml(
            coloredHtmlString,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.buyPowerText.text = spannedText
    }

    private fun supportActionbar() {
        val toolbar = binding.buyUnitToolbar.buyUnitToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.buyElectricity_buyElectricityActivity)
    }

    private fun arrayListOfStates() {
        val states = resources.getStringArray(R.array.States)
        val adapter = ArrayAdapter(
            this,
            R.layout.list_item,
            states
        )
        binding.autoCompleteTextView.setAdapter(adapter)
    }
}