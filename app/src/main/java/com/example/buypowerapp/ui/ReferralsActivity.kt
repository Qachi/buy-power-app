package com.example.buypowerapp.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.buypowerapp.R
import com.example.buypowerapp.databinding.ActivityReferralsBinding
import com.example.buypowerapp.util.Constant
import com.example.buypowerapp.util.showToast

class ReferralsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReferralsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReferralsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBarColor()
        supportActionBar()
        onclickTextCopyListener()
        cardViewBackgroundResource()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun statusBarColor() {
        window.statusBarColor = ContextCompat.getColor(
            this,
            R.color.holo_orange_dark
        )
    }

    private fun supportActionBar() {
        val toolbar = binding.buyUnitToolbar.buyUnitToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.referrals_referralsActivity)
    }

    private fun onclickTextCopyListener() {
        val textview: TextView = binding.textCopy
        textview.setOnClickListener {
            val clipboard: ClipboardManager =
                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData: ClipData =
                ClipData.newPlainText(
                    Constant.CLIP_DATA_LABEL,
                    binding.textCopy.text.toString()
                )
            clipboard.setPrimaryClip(clipData)
          showToast(getString(R.string.copied_to_clipboard))
        }
    }

    private fun cardViewBackgroundResource() {
        val cardView: CardView = binding.cardViewReferrals
        cardView.setBackgroundResource(R.drawable.cardviewreferral)
    }
}