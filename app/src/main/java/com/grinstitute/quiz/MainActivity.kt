package com.grinstitute.quiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.internal.ToolbarUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.grinstitute.quiz.database.DataBaseManager
import com.grinstitute.quiz.databinding.ActivityMainBinding
import com.grinstitute.quiz.frag.Home
import com.grinstitute.quiz.frag.Test
import com.grinstitute.quiz.popup.AboutUsPrivacyDialog
import com.grinstitute.quiz.util.*

class MainActivity : AppCompatActivity() {
    lateinit var actionBarToggle: ActionBarDrawerToggle
    companion object {
        var dataBaseManager = DataBaseManager()
        lateinit var selectCategory: String
    }
    lateinit var binding: ActivityMainBinding
    private lateinit var networkObserver: NetworkObserver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(applicationContext)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this) {
            val fragmentManager = supportFragmentManager
            if (fragmentManager.backStackEntryCount > 0) {
                fragmentManager.popUp(this@MainActivity)
            } else {
                finishAffinity()
            }
        }
        ContextCompat.getColor(this, R.color.quiz_primary_dark)
            .also {
                @Suppress("DEPRECATION")
                window.statusBarColor = it
            }
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
        supportFragmentManager.beginTransaction().replace(R.id.fragmentMain, Home()).addToBackStack(null).commit()
        binding.apply {
            setSupportActionBar(binding.toolbar)
            actionBarToggle = ActionBarDrawerToggle(this@MainActivity, main,toolbar, 0, 0)
            actionBarToggle.drawerArrowDrawable.color = ContextCompat.getColor(applicationContext, android.R.color.white)
            main.addDrawerListener(actionBarToggle)
            main.isSelected = true
            actionBarToggle.syncState()

            navView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_home -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fragmentMain, Home()).addToBackStack(null).commit()
                        main.close()
                        true
                    }
                    R.id.nav_about_us -> {
                        AboutUsPrivacyDialog(this@MainActivity).showAboutUs()
                        main.close()
                        true
                    }
                    R.id.nav_privacy_policy -> {
                        AboutUsPrivacyDialog(this@MainActivity).showPrivacyPolicy()
                        main.close()
                        true
                    }
                    R.id.nav_instasavex -> {
                        launchApp("com.instasavex.media")
                        main.close()
                        true
                    }
                    R.id.nav_more_apps -> {
                        startActivity(Intent(Intent.ACTION_VIEW, getString(R.string.play_more_app).toUri()))
                        main.close()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        networkObserver = NetworkObserver(this)

        // Observe network status
        networkObserver.observe(this, Observer { isConnected ->
            if (!isConnected) {
                showSnackbar(getString(R.string.you_are_offline))
            }
        })
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }
}