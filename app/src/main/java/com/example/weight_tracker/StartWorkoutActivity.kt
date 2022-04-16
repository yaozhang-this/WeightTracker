package com.example.weight_tracker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class StartWorkoutActivity : AppCompatActivity() {
    lateinit var workoutSeekBar: SeekBar
    lateinit var workoutText: TextView
    lateinit var workoutCountDownTimer: CountDownTimer
    lateinit var workoutStartButton : Button

    val defaultTime = 1800
    var time = 0
    var counterActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_workout)
        setTitle("Start Workout")
        workoutText = findViewById(R.id.timerInput)
        workoutSeekBar = findViewById(R.id.timerSeekbar)
        workoutText.setText("00:30:00")
        workoutSeekBar.max = 2700
        workoutSeekBar.progress = defaultTime
        workoutStartButton = findViewById(R.id.startWorkoutButton)

        workoutSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean){
                updateTimer(progress)
            }
            override fun onStartTrackingTouch(p0: SeekBar?){

            }
            override fun onStopTrackingTouch(p0: SeekBar?){

            }
        })
    }//onCreate
    fun onCancel(view: View){
        finish()
    }
    fun updateTimer(secondsLeft : Int) {
        val hours = secondsLeft /3600
        val minutes = (secondsLeft - hours * 3600) /60
        val seconds = secondsLeft - hours * 3600 - minutes * 60
        var secondString = seconds.toString()
        var hourString = hours.toString()
        var minuteString = minutes.toString()

        if (seconds <= 9) {
            secondString = "0$secondString"
        }
        if (minutes <= 9) {
            minuteString = "0$minuteString"
        }
        if (hours <= 9) {
            hourString = "0$hourString"
        }
        workoutText.text = hourString + ":" + minuteString + ":" + secondString
    }//updateTimer

    fun workoutStartTimer (view: View) {
        if (!counterActive) {
            counterActive = true
            workoutStartButton.text = "Stop"
            time = workoutSeekBar.progress * 1000 + 100
            workoutCountDownTimer = object : CountDownTimer(time.toLong(), 1000) {
                override fun onTick(p0: Long) {
                    updateTimer((p0 / 1000).toInt())
                }

                override fun onFinish() {
                    AlertDialog.Builder(view.context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        . setTitle("Workout Ended")
                        .setMessage("Time is up!")
                        .setNegativeButton("OK", null)
                        .show()
                    val mp: MediaPlayer
                    mp = MediaPlayer.create(applicationContext, R.raw.alarm)
                    mp.start()
                    workoutStartTimer(view)
                }
            }
            workoutCountDownTimer.start()

            workoutSeekBar.setVisibility(View.INVISIBLE)
            workoutStartButton.text = "Stop Timer"
        }
        else{
            workoutCountDownTimer.cancel()
            counterActive = false
            workoutStartButton.text = "Start"
            workoutSeekBar.setVisibility(View.VISIBLE)
            workoutText.text = "00:45:00"
            workoutSeekBar.setProgress(defaultTime)
        }
    }//workoutStartTimer


}