package com.grinstitute.quiz.util

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.grinstitute.quiz.MainActivity
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.model.Category
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.databinding.ReportQnaPopupBinding
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Utils {
    companion object {
        fun copyToClipboard(context: Context, text: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(context.getString(R.string.app_name), text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        fun shareText(context: Context, text: String) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        fun parseQnA(context: Context, data: Question): String {
            val question = "Q. ${data.question}"

            val optionLabels = listOf("A.", "B.", "C.", "D.")
            val optionsFormatted = data.options.mapIndexed { index, option ->
                "${optionLabels.getOrNull(index) ?: "${('A' + index)}."} $option"
            }.joinToString("\n")

            val answerIndex = data.answer.toInt()
            val correctAnswer = if (answerIndex in data.options.indices) {
                "Answer: ${optionLabels.getOrNull(answerIndex)} ${data.options[answerIndex]}"
            } else {
                "Answer: Not available"
            }

            val credits = "For More Questions Like This Download ${context.getString(R.string.app_name)} App Now"

            return "$question\n$optionsFormatted\n\n$correctAnswer\n\n$credits"
        }

        fun showIssueReportDialog(context: Context,position: Int,question: Question, topicList: ArrayList<Category>) {
            // Inflate the custom layout
            val binding = ReportQnaPopupBinding.inflate(LayoutInflater.from(context))
            val dialogView: View = binding.root

            // Find the ListView and set up the adapter
            val listViewIssues: ListView = binding.listViewIssues
            val issues = arrayOf<String?>("Question", "Options", "Answer", "Other")
            val adapter = ArrayAdapter<String?>(
                context,
                android.R.layout.simple_list_item_single_choice,
                issues
            )
            listViewIssues.setAdapter(adapter)
            listViewIssues.setChoiceMode(ListView.CHOICE_MODE_SINGLE)

            // Create the dialog
            val builder = MaterialAlertDialogBuilder(context)
            builder.setTitle("Report an Issue")
                .setView(dialogView)
                .setBackground(AppCompatResources.getDrawable(context, R.drawable.popup_bg))
                .setPositiveButton(
                    "Submit",
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->

                        // Handle the submission
                        var selectedIssue: String? = ""
                        val selectedPosition = listViewIssues.getCheckedItemPosition()
                        if (selectedPosition != ListView.INVALID_POSITION) {
                            selectedIssue = issues[selectedPosition]
                        }
                        val name =
                            (dialogView.findViewById(R.id.editTextName) as TextInputEditText).getText()
                                .toString()
                        val email =
                            (dialogView.findViewById(R.id.editTextEmail) as TextInputEditText).getText()
                                .toString()
                        val reason =
                            (dialogView.findViewById(R.id.editTextReason) as TextInputEditText).getText()
                                .toString()

                        CoroutineScope(Dispatchers.IO).launch {
                            val client = HttpClient(CIO)

                            val formData = listOf(
                                "entry.340976912" to MainActivity.selectCategory,
                                "entry.326955045" to topicList.find { it.id == question.cid }?.name,
                                "entry.1870585911" to (position+1).toString(),
                                "entry.1696159737" to reason,
                                "entry.485428648" to name,
                                "entry.879531967" to email,
                                "entry.1591633300" to selectedIssue
                            ).joinToString("&") { (key, value) ->
                                "${key.encodeURLParameter()}=${value?.encodeURLParameter()}"
                            }

                            var toastText = "Couldn't Report Issue"

                            try {
                                val response: HttpResponse = client.post("https://docs.google.com/forms/d/e/1FAIpQLSf3fWeQo78zuKsLG9yGQbPJU8hf1uoVBsobvM3rwyljoQeIBw/formResponse") {
                                    contentType(ContentType.Application.FormUrlEncoded)
                                    setBody(formData)
                                }
                                if(response.status.value == 200) {
                                    toastText = "Issue Reported"
                                }
//                                val bodyText = response.bodyAsText()
//                                println("Response: $bodyText")
                            } catch (e: Exception) {
                                toastText = ("Error : ${e.localizedMessage}")
                            } finally {
                                client.close()
                            }

                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                .setNegativeButton(
                    "Cancel",
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int -> dialog!!.dismiss() })
                .show()
        }

        fun showToast(context: Context, message: String) {
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
        }
    }
}