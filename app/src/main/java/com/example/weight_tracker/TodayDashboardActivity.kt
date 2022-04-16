package com.example.weight_tracker

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.time.LocalDate

class TodayDashboardActivity : AppCompatActivity() {
    lateinit var calories : TextView
    lateinit var days : TextView
    lateinit var list: ListView
    lateinit var arrayAdapter : ArrayAdapter<Calories>
    lateinit var todayWeight : EditText
    lateinit var reminder : TextView
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }//onCreateOptionsMenu

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.log -> {
                val intent = Intent(this, WeightLogActivity::class.java)
                startActivity(intent)
            }
            R.id.journey -> {
                val intent = Intent(this, WeightJourneyActivity::class.java)
                startActivity(intent)
            }
            R.id.set -> {
                AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    . setTitle("Reset log and set new goal?")
                    .setMessage("This action can not be undone.")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener()
                    {
                            dialogInterface: DialogInterface?, i: Int ->
                        sharedPreferences.edit().remove("weightList").apply()
                        sharedPreferences.edit().remove("caloriesList").apply()
                        sharedPreferences.edit().remove("goalDate").apply()
                        sharedPreferences.edit().remove("targetWeight").apply()
                        sharedPreferences.edit().remove("caloriesDate").apply()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    })
                    .setNegativeButton("No", null)
                    .show()
            }
        }
        return true
    } //end onOptionsItemSelected

    override fun onRestart() {
        super.onRestart()
        sharedPreferences.edit().remove("caloriesList").apply()
        sharedPreferences.edit().putString("caloriesList", ObjectSerializer.serialize(caloriesList)).apply()
        getData()
        arrayAdapter.notifyDataSetChanged()
    }

    fun getData() {
        sharedPreferences = getSharedPreferences("com.example.hw5_final", MODE_PRIVATE)
        try {
            goalDate = sharedPreferences.getString("goalDate", "").toString()

        }
        catch (e: Exception){

        }
        try {
            if (goalDate != "") {
                System.out.println("LocalDate is ${LocalDate.now()}")
                days.setText(calculateCountdown(LocalDate.now(), LocalDate.parse(goalDate)).toString())
            }
            if (targetWeight != 0)
            {
                val latestWeight = weightList.get(weightList.size-1).weight.toInt()
                System.out.println(latestWeight)
                val limit = calculateCaloriesDelta(latestWeight, targetWeight)
                var sum = 0;
                for ( c in caloriesList){
                    if (c.type == "food")
                        sum += c.calories.toInt()
                    else{
                        sum -= c.calories.toInt()
                    }
                }
                if ((limit - sum) < 0) reminder.setText("Calories left to endulge today, \ngo do some workout!")
                else if ((limit - sum) > 1000) reminder.setText("Calories left to endulge today, \nkeep it up!")
                else reminder.setText("Calories left to endulge today")
                calories.setText((limit - sum).toString())
            }

        }
        catch (e: Exception){
        }
    }//getData

    fun addCalories(view : View){
        val intent = Intent(this, AddCaloriesActivity::class.java)
        startActivity(intent)

    }

    fun startWorkout( view: View ){
        val intent = Intent(this, StartWorkoutActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_dashboard)
        setTitle("Dashboard")
        calories = findViewById(R.id.calories)
        days = findViewById(R.id.days)
        list = findViewById(R.id.caloriesList)
        todayWeight = findViewById(R.id.currentWeightDisplay)
        reminder = findViewById(R.id.reminder)
        if(weightList[weightList.size-1].date == LocalDate.now().toString()){
            todayWeight.setText(weightList[weightList.size-1].weight)
        }
        todayWeight.setOnFocusChangeListener(object: View.OnFocusChangeListener{
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if(!p1){
                    if (todayWeight.text.toString() != "" && todayWeight.text.toString().toInt() != 0)
                    {
                        AlertDialog.Builder(p0!!.context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            . setTitle("Record today's weight of ${todayWeight.text}?")
                            .setPositiveButton("Yes", DialogInterface.OnClickListener()
                            {
                                    dialogInterface: DialogInterface?, i: Int ->
                                val newWeight = Weight(todayWeight.text.toString(), LocalDate.now().toString())
                                if(weightList[weightList.size-1].date != LocalDate.now().toString())
                                {
                                    weightList.add(newWeight)
                                    arrayAdapter.notifyDataSetChanged()
                                    sharedPreferences.edit().remove("weightList").apply()
                                    sharedPreferences.edit().putString("weightList", ObjectSerializer.serialize(weightList)).apply()
                                    getData()
                                }
                                else{
                                    weightList.set(weightList.size-1,newWeight)
                                    arrayAdapter.notifyDataSetChanged()
                                    sharedPreferences.edit().remove("weightList").apply()
                                    sharedPreferences.edit().putString("weightList", ObjectSerializer.serialize(weightList)).apply()
                                    getData()
                                }
                                if ( newWeight.weight.toInt() == targetWeight) {
                                    moveScreens()
                                }
                                if (targetWeight > weightList[0].weight.toInt()){
                                    if (newWeight.weight.toInt() >= targetWeight){
                                        moveScreens()
                                    }
                                }
                                else{
                                    if (newWeight.weight.toInt() <= targetWeight){
                                        moveScreens()
                                    }
                                }
                            })
                            .setNegativeButton("No", null)
                            .show()
                    }
                }
            }
        })
        getData()
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, caloriesList)
        list.adapter = arrayAdapter

    } // onCreate

    fun moveScreens(){
        val intent = Intent(this, TargetActivity::class.java)
        startActivity(intent)
    }

}
