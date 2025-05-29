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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.chat)
        button.setOnClickListener {
            showResultDialog()
        }
    }

    private fun showResultDialog() {
        activity?.let { fragmentActivity ->
            AlertDialog.Builder(fragmentActivity)
                .setTitle("Результат")
                .setMessage("ВАШ РЕЗУЛЬТАТ: ***")
                .setPositiveButton("ОК", null)
                .create()
                .show()
        }
    }
}

class History : Fragment(R.layout.history) {}

