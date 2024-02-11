package com.example.buypowerapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.buypowerapp.R
import com.example.buypowerapp.ViewPagerAdapter
import com.example.buypowerapp.databinding.ActivityMainBinding
import com.example.buypowerapp.fragment.BlankFragment1
import com.example.buypowerapp.fragment.BlankFragment2
import com.example.buypowerapp.fragment.BlankFragment3
import com.example.buypowerapp.ui.*
import com.example.buypowerapp.util.Constant
import com.example.buypowerapp.util.showToast
import com.example.buypowerapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    @Inject
    lateinit var mainViewModel: MainViewModel

    @SuppressLint("SetTextI18n", "InflateParams", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBarColor()
        cardViewsOnClickListener()
        buyPowerText()
        supportActionbar()
        updateWelcomeMessage()

        // Display username on dashboard from firebase
        val currentUser = mainViewModel.firebaseAuth.currentUser
        val userId = currentUser?.uid
        if (userId != null) {
            mainViewModel.getUserFirstName(
                userId,
                { fullName ->
                    val signedInUserText = findViewById<TextView>(R.id.welcomeUserTextView)
                    signedInUserText.text = "${Constant.WELCOME_MESSAGE} $fullName"
                },
                { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        }

        navMenuTextSize()
        supportActionbar()
        toggleDrawer()
        viewPager()
        navigationMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun autoscroll(fragments: ArrayList<Fragment>) {
        val viewpager = binding.cardViewsConstrainLayout.viewPage
        val handler = Handler(Looper.getMainLooper())
        val timer = Timer(false)
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    if (viewpager.currentItem == fragments.size - 1) {
                        viewpager.currentItem = 0
                    } else {
                        viewpager.currentItem = viewpager.currentItem + 1
                    }
                }
            }
        }
        timer.scheduleAtFixedRate(
            timerTask,
            Constant.TIMER_DELAY,
            Constant.TIMER_PERIOD
        )
    }

    private fun logout() {
        mainViewModel.firebaseAuth.signOut()
        startActivity(
            Intent(
                this,
                WelcomeBackActivity::class.java
            )
        )
        finish()
    }

    private fun buyUnit() {
        val intent = Intent(
            this,
            BuyUnitActivity::class.java
        )
        startActivity(intent)
    }

    private fun orderHistory() {
        val intent = Intent(
            this,
            OrderHistoryActivity::class.java
        )
        startActivity(intent)
    }

    private fun referralsActivity() {
        val intent = Intent(
            this,
            ReferralsActivity::class.java
        )
        startActivity(intent)
    }

    private fun contactUsActivity() {
        val intent = Intent(
            this,
            ContactUsActivity::class.java
        )
        startActivity(intent)
    }

    private fun cardViewsOnClickListener() {
        binding.cardViewsConstrainLayout.cardViewBuyU.setOnClickListener {
            buyUnit()
        }
        binding.cardViewsConstrainLayout.cardViewOrderH.setOnClickListener {
            orderHistory()
        }
        binding.cardViewsConstrainLayout.cardViewRef.setOnClickListener {
            referralsActivity()
        }
        binding.cardViewsConstrainLayout.cardViewContactU.setOnClickListener {
            contactUsActivity()
        }
    }

    private fun statusBarColor() {
        window.statusBarColor = ContextCompat.getColor(
            this,
            R.color.dim_foreground_holo_light
        )
    }

    //ToolBar Title Multiple Color
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
        binding.cardViewsConstrainLayout.toolBar.toolbarTitleText.text = spannedText
    }

    private fun navigationMenu() {
        val navView = binding.nav
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeMenu -> showToast(getString(R.string.home_clicked))
                R.id.profile -> showToast(getString(R.string.profile_clicked))
                R.id.transaction -> showToast(getString(R.string.transaction_clicked))
                R.id.buy_Elect -> showToast(getString(R.string.buy_electricity_clicked))
                R.id.referrals -> showToast(getString(R.string.referral_clicked))
                R.id.contact_US -> showToast(getString(R.string.contact_us_clicked))
                R.id.reminder -> showToast(getString(R.string.reminder__clicked))
                R.id.meters_con -> showToast(getString(R.string.meter_consumption_clicked))
                R.id.other_Services -> showToast(getString(R.string.other_services_clicked))
                R.id.help_Faq -> showToast(getString(R.string.help_faq_clicked))
                R.id.logout -> logout()
            }
            true
        }
    }

    private fun navMenuTextSize() {
        val navView = binding.nav
        val menu = navView.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val spannableString = SpannableString(menuItem.title.toString())
            spannableString.setSpan(
                AbsoluteSizeSpan(
                    18,
                    true
                ),
                0,
                spannableString.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            menuItem.title = spannableString
        }
    }

    private fun supportActionbar() {
        val toolBarMain = binding.cardViewsConstrainLayout.toolBar
        setSupportActionBar(toolBarMain.toolBar)
        toolBarMain.toolBar.setTitleTextColor(
            ContextCompat.getColor(
                this,
                R.color.colorAccent
            )
        )
        toolBarMain.toolBar.showOverflowMenu()
    }

    private fun toggleDrawer() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.navDrawer,
            R.string.open,
            R.string.close
        )
        toggle.syncState()
        toggle.drawerArrowDrawable.color = getColor(R.color.background_light)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun viewPager() {
        val fragments: ArrayList<Fragment> = arrayListOf(
            BlankFragment1(),
            BlankFragment2(),
            BlankFragment3()
        )
        val viewpager = binding.cardViewsConstrainLayout.viewPage
        val adapter = ViewPagerAdapter(fragments, this)
        viewpager.adapter = adapter
        autoscroll(fragments)
    }

    @SuppressLint("SetTextI18n")
    private fun updateWelcomeMessage() {
        val welcomeUserText = findViewById<TextView>(R.id.welcomeUserTextView)
        val registeredUser = intent.getStringExtra(Constant.REGISTERED_USER_NAME)
        welcomeUserText.text = "${Constant.WELCOME_MESSAGE} $registeredUser"
    }
}
