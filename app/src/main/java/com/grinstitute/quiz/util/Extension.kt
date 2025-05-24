package com.grinstitute.quiz.util

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.grinstitute.quiz.MainActivity
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.model.Category
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.database.model.QuizResult
import com.grinstitute.quiz.databinding.ReportQnaPopupBinding
import com.grinstitute.quiz.frag.Test
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import ir.mahozad.android.PieChart
import ir.mahozad.android.component.Alignment
import ir.mahozad.android.unit.Dimension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    listViewIssues.adapter = adapter
    listViewIssues.choiceMode = ListView.CHOICE_MODE_SINGLE

    // Create the dialog
    val builder = MaterialAlertDialogBuilder(context)
    builder.setTitle("Report an Issue")
        .setView(dialogView)
        .setBackground(AppCompatResources.getDrawable(context, R.drawable.popup_bg))
        .setPositiveButton(
            "Submit"
        ) { _: DialogInterface?, _: Int ->

            // Handle the submission
            var selectedIssue: String? = ""
            val selectedPosition = listViewIssues.checkedItemPosition
            if (selectedPosition != ListView.INVALID_POSITION) {
                selectedIssue = issues[selectedPosition]
            }
            val name =
                (dialogView.findViewById<TextInputEditText>(R.id.editTextName)!!).getText()
                    .toString()
            val email =
                (dialogView.findViewById<TextInputEditText>(R.id.editTextEmail)!!).getText()
                    .toString()
            val reason =
                (dialogView.findViewById<TextInputEditText>(R.id.editTextReason)!!).getText()
                    .toString()

            CoroutineScope(Dispatchers.IO).launch {
                val client = HttpClient(CIO)

                val formData = listOf(
                    "entry.340976912" to MainActivity.selectCategory,
                    "entry.326955045" to topicList.find { it.id == question.cid }?.name,
                    "entry.1870585911" to (position + 1).toString(),
                    "entry.1696159737" to reason,
                    "entry.485428648" to name,
                    "entry.879531967" to email,
                    "entry.1591633300" to selectedIssue
                ).joinToString("&") { (key, value) ->
                    "${key.encodeURLParameter()}=${value?.encodeURLParameter()}"
                }

                var toastText = "Couldn't Report Issue"

                try {
                    val response: HttpResponse =
                        client.post("https://docs.google.com/forms/d/e/1FAIpQLSf3fWeQo78zuKsLG9yGQbPJU8hf1uoVBsobvM3rwyljoQeIBw/formResponse") {
                            contentType(ContentType.Application.FormUrlEncoded)
                            setBody(formData)
                        }
                    if (response.status.value == 200) {
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
        }
        .setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface?, _: Int -> dialog!!.dismiss() }
        .show()
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
}

@SuppressLint("ClickableViewAccessibility")
fun CheckedTextView.setCheckable(boolean: Boolean) {
    this.isClickable = boolean
    if (boolean){
        this.setOnTouchListener(null)
    } else {
        this.setOnTouchListener { _, _ -> true }
    }
}

fun generateQuestions(questionMap: MutableMap<Long, ArrayList<Question>>, topics: ArrayList<Category>, numberOfQuestions: Int = 25): ArrayList<Question> {
    val selectedQuestions = ArrayList<Question>()

    for (topic in topics) {
        val questions = questionMap[topic.id]

        questions?.let {
            selectedQuestions.addAll(it)
        }
    }

    selectedQuestions.shuffle()

    return ArrayList(selectedQuestions.take(numberOfQuestions))
}

fun evaluateQuestions(questions: List<Question>): QuizResult {
    var correct = 0
    var wrong = 0
    var unattempted = 0

    for (q in questions) {
        when (q.selectedOption) {
            null -> unattempted++
            q.answer.toInt() -> correct++
            else -> wrong++
        }
    }

    return QuizResult(
        total = questions.size,
        correct = correct,
        wrong = wrong,
        unattempted = unattempted
    )
}

fun Context.getPrimaryTextColor(): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
    return ContextCompat.getColor(this, typedValue.resourceId)
}


fun setPieChartData(questions: List<Question>, pieChart: PieChart) {
    val result = evaluateQuestions(questions)

    val total = result.total.toFloat().takeIf { it > 0 } ?: 1f

    var slices = listOf(
        PieChart.Slice(
            result.correct / total,
            Color.rgb(120, 181, 0),
            Color.rgb(149, 224, 0),
            legend = "Correct Answers"
        ),
        PieChart.Slice(
            result.wrong / total,
            Color.rgb(255, 4, 4),
            Color.rgb(255, 72, 86),
            legend = "Wrong Answers"
        ),
        PieChart.Slice(
            result.unattempted / total,
            Color.rgb(160, 165, 170),
            Color.rgb(175, 180, 185),
            legend = "Unattempted Questions"
        )
    )

    slices.forEach { if(it.fraction <= 0) slices = slices.minus(it) }

    pieChart.slices = slices

    pieChart.apply {
        gradientType = PieChart.GradientType.RADIAL
        legendIconsMargin = 8.dp
        legendTitleMargin = 14.dp
        legendsTitle = "Total Questions: ${result.total}"
        legendsTitleColor = this.rootView.context.getPrimaryTextColor()
        legendLinesMargin = 10.dp
        legendsMargin = 20.dp
        legendsPercentageMargin = 8.dp
        legendsSize = 11.sp
        legendsPercentageSize = 11.sp
        legendsIcon = PieChart.DefaultIcons.SLICE2
        isLegendEnabled = true
        legendsColor = this.rootView.context.getPrimaryTextColor()
        isLegendBoxBorderEnabled = true
        legendBoxBorder = 2.dp
        legendBoxBorderCornerRadius = 8.dp
        isCenterLabelEnabled = true
        centerLabel = "Score: "+ (result.correct * 100 / total).toInt().toString() + "%"
        centerLabelIcon = PieChart.CustomIcon(R.drawable.badge)
        centerLabelColor = this.rootView.context.getPrimaryTextColor()
        centerLabelIconMargin = 2.dp
        shouldCenterPie = true
        legendBoxAlignment = Alignment.CENTER
        isLegendsPercentageEnabled = true
        legendsPercentageColor = this.rootView.context.getPrimaryTextColor()
        labelType = PieChart.LabelType.INSIDE
        holeRatio = 0.50f
    }
}

val Int.dp: Dimension.DP
    get() = Dimension.DP(this.toFloat())

val Int.sp: Dimension.SP
    get() = Dimension.SP(this.toFloat())

fun FragmentManager.popUp(context: Context) {
    if(this.fragments.last() is Test){
        MaterialAlertDialogBuilder(context)
            .setTitle("Confirm Exit")
            .setMessage("Are you sure you want to exit test?")
            .setPositiveButton("Yes") { dialog, _ ->
                // User confirmed, go back
                this.popBackStack()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                // User cancelled, just dismiss dialog
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    } else this.popBackStack()
}

fun MainActivity.setGoBackButton(){
    this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    this.actionBarToggle.isDrawerIndicatorEnabled = true
    this.binding.toolbar.navigationIcon?.setTint(ContextCompat.getColor(this.applicationContext, R.color.white))
    this.binding.toolbar.setNavigationOnClickListener {
        this.supportFragmentManager.popUp(this)
    }
}
