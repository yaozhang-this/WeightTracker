package com.example.weight_tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView


class WeightLogActivity : AppCompatActivity() {
    lateinit var arrayAdapter : ArrayAdapter<Weight>
    lateinit var list: ListView
    lateinit var todayWorkout : EditText

    private fun updateData(key : String) {
        sharedPreferences = getSharedPreferences("com.example.hw5", MODE_PRIVATE)
        sharedPreferences.edit().remove(key).apply()
        sharedPreferences.edit().putString(key, ObjectSerializer.serialize(weightList)).apply()

    }//updateData
    override fun onRestart(){
        super.onRestart()
        updateData("weights")
        arrayAdapter.notifyDataSetChanged()
    }//onRestart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_log)
        setTitle("Journal with Current Target: $targetWeight lbs")
        if (weightList.size != 0) updateData("weights")
        list = findViewById(R.id.weightList)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weightList)
        list.adapter = arrayAdapter
    }
}