package com.example.myapplication.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
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

        linearLayout.removeAllViews()

        if (history.isNullOrEmpty()) {
            emptyTextView.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.GONE
            scrollView.visibility = View.VISIBLE

            var counter = 1
            history?.forEach {
                val textView = TextView(context)
                textView.text = "Результат №$counter:\n$it"
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
        }

        return view
    }
}