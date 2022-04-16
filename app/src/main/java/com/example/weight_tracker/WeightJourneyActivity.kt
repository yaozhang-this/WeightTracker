package com.example.weight_tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.time.LocalDate

class WeightJourneyActivity : AppCompatActivity() {
    lateinit var averageText : TextView
    lateinit var changesText : TextView
    lateinit var remainingText : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_journey)
        setTitle("Journey Summary")
        averageText = findViewById(R.id.averageText)
        changesText = findViewById(R.id.changesText)
        remainingText = findViewById(R.id.remainingText)
        var changes = weightList[weightList.size-1].weight.toInt() - weightList[0].weight.toInt()
        if (changes < 0) {
            changes *= -1
            changesText.setText("You have lost $changes lbs so far since you started")
        }
        else changesText.setText("You have gained $changes lbs so far since you started")
        var remaining = targetWeight - weightList[weightList.size-1].weight.toInt()
        val days = calculateCountdown(LocalDate.now(), LocalDate.parse(goalDate))
        if (remaining < 0)
        {
            remaining *= -1
            remainingText.setText("You have $remaining lbs to lose in $days days")
        }
        else
        {
            remainingText.setText("You have $remaining lbs to gain in $days days")
        }
        var average = 0
        for ( d in digestList){
            average += d.caloriesChanged.toInt()
        }
        if (digestList.size != 0)average /= digestList.size;
        averageText.setText("On average you change $average calories per day")
    }
}