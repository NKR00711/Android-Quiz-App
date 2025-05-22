package com.grinstitute.quiz

import android.app.Application
import android.content.Intent
import android.os.Process
import com.google.firebase.FirebaseApp
import kotlin.system.exitProcess

class QuizApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        Thread.setDefaultUncaughtExceptionHandler { _, _ ->
            val intent = Intent(
                applicationContext,
                MainActivity::class.java
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            Process.killProcess(Process.myPid())
            exitProcess(1)
        }
    }
}