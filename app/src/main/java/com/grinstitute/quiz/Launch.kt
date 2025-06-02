package com.grinstitute.quiz

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.grinstitute.quiz.databinding.ActivityLaunchBinding

class Launch : AppCompatActivity() {
    private lateinit var appUpdateResultLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var activitySplashBinding: ActivityLaunchBinding
    private lateinit var appUpdateManager: AppUpdateManager
    private var startApp = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashBinding = ActivityLaunchBinding.inflate(
            layoutInflater
        )
        setContentView(activitySplashBinding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        updateApp()
        val animOne = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_zoom_in)
        val animTwo = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_zoom_out)
        activitySplashBinding.logo.startAnimation(animOne)
        animOne.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                // Animation started
            }

            override fun onAnimationEnd(animation: Animation) {
                // Start the second animation when the first one completes
                if(startApp){
                    homeScreen()
                } else activitySplashBinding.logo.startAnimation(animTwo)
            }

            override fun onAnimationRepeat(animation: Animation) {
                // Animation repeated
            }
        })
        animTwo.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                activitySplashBinding.logo.startAnimation(animOne)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }
    private fun homeScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this@Launch, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 1000)
    }

    private fun updateApp() {
        appUpdateResultLauncher = registerForActivityResult<IntentSenderRequest, ActivityResult>(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result: ActivityResult ->
            if (result.resultCode != RESULT_OK) {
                startApp = true
            } else {
                startApp = true
            }
        }
        try {
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        appUpdateResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                } else {
                    startApp = true
                }
            }.addOnFailureListener { e: Exception ->
                e.printStackTrace()
                startApp = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}