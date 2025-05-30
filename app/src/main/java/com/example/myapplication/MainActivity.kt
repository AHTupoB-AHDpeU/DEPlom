package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.databinding.ActivityMainBinding
import androidx.fragment.app.Fragment
import android.app.AlertDialog
import android.os.SystemClock
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.graphics.Color
import android.widget.EditText
import android.widget.TextView
import kotlin.math.roundToInt
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        // связываем bottomNavigationView и navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

}

class Main : Fragment(R.layout.main) {

    private var startTime: Long = 0
    private var hasStarted = false
    private lateinit var targetText: String
    private lateinit var textView: TextView
    private lateinit var editText: EditText
    private var errorCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.chat)
        textView = view.findViewById(R.id.textViewTop)
        editText = view.findViewById(R.id.editTextInput)

        targetText = "Если хочешь депнуть мне - давай скорей. Ну если хватит на додеп, то депни всё. Да нам медлить ни к чему, давай въебём всё. Всё окупится, поверь. Само собой."

        textView.text = targetText

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!hasStarted && !s.isNullOrEmpty()) {
                    startTime = SystemClock.elapsedRealtime()
                    hasStarted = true
                }

                val userInput = s.toString()
                val colored = SpannableStringBuilder(targetText)
                errorCount = 0

                for (i in targetText.indices) {
                    if (i < userInput.length) {
                        if (userInput[i] == targetText[i]) {
                            colored.setSpan(
                                ForegroundColorSpan(Color.GREEN),
                                i, i + 1,
                                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        } else {
                            colored.setSpan(
                                ForegroundColorSpan(Color.RED),
                                i, i + 1,
                                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            errorCount++
                        }
                    } else {
                        colored.setSpan(
                            ForegroundColorSpan(Color.GRAY),
                            i, i + 1,
                            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }

                textView.text = colored

                // Проверка на окончание
                if (userInput.length >= targetText.length) {
                    val timeSpent = SystemClock.elapsedRealtime() - startTime
                    val timeInMinutes = timeSpent / 60000.0
                    val charsPerMinute = (targetText.length / timeInMinutes).roundToInt()
                    val accuracy = 100 - ((errorCount * 100.0) / targetText.length).roundToInt()

                    showResultDialog(charsPerMinute, accuracy)
                    hasStarted = false
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        button.setOnClickListener {
            // Сброс
            editText.setText("")
            textView.text = targetText
            hasStarted = false
        }

    }

    private fun showResultDialog(speed: Int, accuracy: Int) {
        val currentTime = SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault()).format(Date())
        val result = "Время: $currentTime \nСкорость: $speed зн./мин \nТочность: $accuracy%\nОшибок: $errorCount"
        saveResultToHistory(result)

        AlertDialog.Builder(requireContext())
            .setTitle("Результат")
            .setMessage(result)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()

        editText.setText("")
        textView.text = targetText
        hasStarted = false
    }

    private fun saveResultToHistory(result: String) {
        val sharedPref = activity?.getSharedPreferences("history_pref", AppCompatActivity.MODE_PRIVATE)
        val history = sharedPref?.getStringSet("history_set", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        history.add(result)

        sharedPref?.edit()?.putStringSet("history_set", history)?.apply()
    }
}

class History : Fragment(R.layout.history) {}

