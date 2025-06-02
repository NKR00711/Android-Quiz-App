package com.grinstitute.quiz.popup

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.grinstitute.quiz.databinding.AboutUsBinding
import com.grinstitute.quiz.databinding.PrivacyPolicyBinding
import java.io.IOException
import java.util.Objects
import androidx.core.graphics.drawable.toDrawable
import com.grinstitute.quiz.R

class AboutUsPrivacyDialog(private val context: Context) {
    @SuppressLint("SetTextI18n")
    fun showAboutUs() {
        // Create a layout inflater to inflate the custom layout
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val aboutUsBinding = AboutUsBinding.inflate(inflater)
        // Create an AlertDialog
        val builder = AlertDialog.Builder(context)
        builder.setView(aboutUsBinding.root)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        Objects.requireNonNull<Window>(alertDialog.window).setBackgroundDrawable(
            Color.TRANSPARENT.toDrawable()
        )
        aboutUsBinding.textViewAppVersionAboutUs.text = getAppVersion(context)

        aboutUsBinding.textViewAppEmailAboutUs.text = "nkr00711@gmail.com"

        aboutUsBinding.textViewAppdesAboutUs.text = context.getString(R.string.app_description)

        // Set a click listener for the close button
        aboutUsBinding.btnClose.setOnClickListener {
            alertDialog.dismiss()
        }

        // Show the dialog
        alertDialog.show()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun showPrivacyPolicy() {
        // Create a layout inflater to inflate the custom layout
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var privacyPolicyBinding = PrivacyPolicyBinding.inflate(inflater)
        // Create an AlertDialog
        val builder = AlertDialog.Builder(context)
        builder.setView(privacyPolicyBinding.root)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        Objects.requireNonNull<Window>(alertDialog.window).setBackgroundDrawable(
            Color.TRANSPARENT.toDrawable()
        )
        val str: String = try {
            val string = context.resources.openRawResource(R.raw.privacypolicy)
            val size = string.available()
            val buffer = ByteArray(size) // Read the entire asset into a local byte buffer.
            string.read(buffer)
            string.close()
            String(buffer) // Convert the buffer into a string.
        } catch (e: IOException) {
            throw RuntimeException(e) // Should never happen!
        }

        privacyPolicyBinding.webViewPrivacyPolicy.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        privacyPolicyBinding.webViewPrivacyPolicy.isFocusableInTouchMode = false
        privacyPolicyBinding.webViewPrivacyPolicy.isFocusable = false
        privacyPolicyBinding.webViewPrivacyPolicy.settings.defaultTextEncodingName = "UTF-8"
        privacyPolicyBinding.webViewPrivacyPolicy.settings.javaScriptEnabled = true

        val mimeType = "text/html"
        val encoding = "utf-8"

        privacyPolicyBinding.webViewPrivacyPolicy.loadDataWithBaseURL(null, str, mimeType, encoding, null)
        // Set a click listener for the close button
        privacyPolicyBinding.btnClose.setOnClickListener {
            alertDialog.dismiss()
        }

        // Show the dialog
        alertDialog.show()
    }

    fun getAppVersion(context: Context): String {
        try {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "N/A" // Default value if the version is not found
    }
}