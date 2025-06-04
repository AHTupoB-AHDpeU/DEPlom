package com.example.typefast.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.typefast.R
import android.content.Context

class History : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.history, container, false)
        val linearLayout = view.findViewById<LinearLayout>(R.id.historyContainer)
        val sharedPref = activity?.getSharedPreferences("history_pref", Context.MODE_PRIVATE)
        val history = sharedPref?.getStringSet("history_set", setOf())?.toList()?.sorted()
        val emptyTextView = view.findViewById<TextView>(R.id.emptyHistoryText)
        val scrollView = view.findViewById<View>(R.id.scrollView)

        val bestResultText = view.findViewById<TextView>(R.id.bestResultText)
        val avgResultText = view.findViewById<TextView>(R.id.avgResultText)

        linearLayout.removeAllViews()

        if (history.isNullOrEmpty()) {
            emptyTextView.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
            bestResultText.visibility = View.GONE
            avgResultText.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.GONE
            scrollView.visibility = View.VISIBLE

            // Лучший результат
            var bestEntry: String? = null
            var bestSpeed = -1
            var bestAccuracy = -1

            for (entry in history) {
                val speedRegex = Regex("""${getString(R.string.speed)} (\d+)""")
                val accRegex = Regex("""${getString(R.string.accuracy)} (\d+)%""")

                val speed = speedRegex.find(entry)?.groupValues?.get(1)?.toIntOrNull() ?: 0
                val accuracy = accRegex.find(entry)?.groupValues?.get(1)?.toIntOrNull() ?: 0


                if (bestEntry == null) {
                    bestEntry = entry
                    bestSpeed = speed
                    bestAccuracy = accuracy
                    continue
                }
                when {
                    accuracy >= bestAccuracy + 5 -> {
                        bestEntry = entry
                        bestSpeed = speed
                        bestAccuracy = accuracy
                    }
                    kotlin.math.abs(accuracy - bestAccuracy) < 5 && speed > bestSpeed -> {
                        bestEntry = entry
                        bestSpeed = speed
                        bestAccuracy = accuracy
                    }
                    accuracy == bestAccuracy && speed == bestSpeed -> {
                        bestEntry = entry
                    }
                }
            }

            // Расчёт средней скорости и точности
            var totalSpeed = 0
            var totalAccuracy = 0
            var count = 0

            val speedRegex = Regex("""${getString(R.string.speed)} (\d+)""")
            val accRegex = Regex("""${getString(R.string.accuracy)} (\d+)%""")

            for (entry in history) {
                val speed = speedRegex.find(entry)?.groupValues?.get(1)?.toIntOrNull() ?: continue
                val accuracy = accRegex.find(entry)?.groupValues?.get(1)?.toIntOrNull() ?: continue

                totalSpeed += speed
                totalAccuracy += accuracy
                count++
            }

            if (count > 0) {
                val avgSpeed = totalSpeed / count
                val avgAccuracy = totalAccuracy / count
                avgResultText.visibility = View.VISIBLE
                avgResultText.text = "${getString(R.string.avg_result)}:\n${getString(R.string.speed)} $avgSpeed ${getString(R.string.signs)}\n${getString(R.string.accuracy)} $avgAccuracy%"
            } else {
                avgResultText.visibility = View.GONE
            }


            if (bestEntry != null) {
                bestResultText.visibility = View.VISIBLE
                bestResultText.text = "${getString(R.string.best_result)}:\n$bestEntry"
            } else {
                bestResultText.visibility = View.GONE
            }

            var counter = 1
            history?.forEach {
                val textView = TextView(context)
                textView.text = "${getString(R.string.result)} $counter:\n$it"
                textView.textSize = 18f
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 8, 0, 8)
                textView.layoutParams = params
                linearLayout.addView(textView)

                counter++
            }
        }

        // Кнопка
        val btnClear = view.findViewById<Button>(R.id.btnClearHistory)
        btnClear.setOnClickListener {
            sharedPref?.edit()?.remove("history_set")?.apply()
            linearLayout.removeAllViews()
            emptyTextView.visibility = View.VISIBLE
            scrollView.visibility = View.GONE

            bestResultText.visibility = View.GONE
            avgResultText.visibility = View.GONE
        }

        return view
    }
}