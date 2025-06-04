package com.example.typefast.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.typefast.R
import com.example.typefast.data.Achievement

class AchievementAdapter(
    private val achievements: List<Achievement>
) : RecyclerView.Adapter<AchievementAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon = view.findViewById<ImageView>(R.id.iconAchievement)
        val title = view.findViewById<TextView>(R.id.titleAchievement)
        val desc = view.findViewById<TextView>(R.id.descAchievement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_achievement, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = achievements.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val achievement = achievements[position]
        holder.title.text = achievement.title
        holder.desc.text = achievement.description

        if (achievement.achieved) {
            holder.icon.setImageResource(R.drawable.ic_trophy_color) // цветная иконка
            holder.title.setTextColor(Color.WHITE)
            holder.desc.setTextColor(Color.LTGRAY)
        } else {
            holder.icon.setImageResource(R.drawable.ic_trophy_gray) // серая
            holder.title.setTextColor(Color.GRAY)
            holder.desc.setTextColor(Color.GRAY)
        }
    }
}
