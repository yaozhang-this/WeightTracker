package com.example.weight_tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class TargetActivity : AppCompatActivity() {
    lateinit var congrats : ImageView
    lateinit var text : TextView
    //lateinit var mp : MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)
        setTitle("Congratulations!")
        text = findViewById(R.id.textView2)
        text.setText("$targetWeight lbs")
        //sharedPreferences.edit().remove("baseline").apply()
        //sharedPreferences.edit().putInt("baseline", weightList[weightList.size-1].weight.toInt()).apply()
        congrats = findViewById(R.id.congrats)
        congrats.animate().translationYBy(-900f)
        congrats.animate().alpha(1f).translationYBy(900f).rotation(360f).duration = 2000
        //mp = MediaPlayer.create(applicationContext, R.raw.sound)
        //mp.start()
    }

    fun reset(view: View){
        //mp.stop()
        sharedPreferences.edit().remove("weightList").apply()
        sharedPreferences.edit().remove("caloriesList").apply()
        sharedPreferences.edit().remove("goalDate").apply()
        sharedPreferences.edit().remove("targetWeight").apply()
        sharedPreferences.edit().remove("caloriesDate").apply()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}