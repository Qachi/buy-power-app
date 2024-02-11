package com.example.buypowerapp.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.buypowerapp.R
import com.example.buypowerapp.databinding.ActivityContactUsBinding
import com.example.buypowerapp.util.showToast
import com.google.android.material.textfield.TextInputLayout

class ContactUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBarColor()
        buyPowerTextView()
        textInputDescriptionLayoutHint()
        descriptionButton()
        supportActionbar()
        contactUsArrayList()
        contactUsNumber()
        contactUsTextView()
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

    private fun buyPowerTextView() {
        val htmlString = getString(R.string.buy_power_html_string)
        val coloredHtmlString = String.format(
            htmlString,
            getColor(R.color.primary),
            getColor(R.color.holo_green_light)
        )
        val spannedText = HtmlCompat.fromHtml(
            coloredHtmlString,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.textViewContactUs.text = spannedText
    }

    private fun descriptionButton() {
        binding.send.setOnClickListener {
            showToast(getString(R.string.message_sent))
        }
    }

    private fun supportActionbar() {
        val toolbar = binding.buyUnitToolbar.buyUnitToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.contactus_contactus_activity)
    }

    private fun contactUsArrayList() {
        val contactus = resources.getStringArray(R.array.ContactUs)
        val adapter = ArrayAdapter(
            this,
            R.layout.list_item,
            contactus
        )
        binding.autoCompleteTextViewContactUs.setAdapter(adapter)
    }

    private fun textInputDescriptionLayoutHint() {
        val textInputLayout: TextInputLayout = binding.descriptionTextInputLayout
        textInputLayout.hint = getString(R.string.description_contactus_activity)
    }

    private fun contactUsTextView() {
        val weAreHereTextViewW = resources.getFont(R.font.roboto_medium)
        binding.weAreHereTextView.typeface = weAreHereTextViewW
    }

    private fun contactUsNumber() {
        binding.textViewContactUsNumber.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}
