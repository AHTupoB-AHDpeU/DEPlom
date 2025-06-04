package com.example.typefast.ui.achievements

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.typefast.R
import com.example.typefast.adapter.AchievementAdapter
import com.example.typefast.data.Achievement

class Achievements : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AchievementAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.achievements, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.achievementsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val sharedPref = requireActivity().getSharedPreferences("achievements", Context.MODE_PRIVATE)
        val hasFirstResult = sharedPref.getBoolean("hasFirst", false)
        val has100Percent = sharedPref.getBoolean("has100", false)

        val achievements = listOf(
            Achievement(
                "first_result",
                getString(R.string.achievement_1_title),
                getString(R.string.achievement_1_desc),
                hasFirstResult
            ),
            Achievement(
                "perfect_accuracy",
                getString(R.string.achievement_2_title),
                getString(R.string.achievement_2_desc),
                has100Percent
            )
        )

        adapter = AchievementAdapter(achievements)
        recyclerView.adapter = adapter
    }
}
