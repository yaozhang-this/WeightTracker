package com.example.weight_tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import java.time.LocalDate

class AddCaloriesActivity : AppCompatActivity() {
    var type = "food"
    lateinit var caloriesChange : EditText
    lateinit var logName : EditText
    lateinit var saveButton : Button
    lateinit var cancelButton : Button
    var checkedType : RadioButton? = null
    var caloriesChanged = 0
    var description = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_calories)
        setTitle("Add Calories Record")
        caloriesChange = findViewById(R.id.caloriesDelta)
        logName = findViewById(R.id.caloriesName)
        saveButton = findViewById(R.id.save)
        cancelButton = findViewById(R.id.cancel)
    }

    fun chooseType(view: View?){
        checkedType = (view as RadioButton)
        when(view.getId()){
            R.id.food -> type = "food"
            R.id.workout -> type = "workout"
        }//when
    }//chooseType

    private fun updateCaloriesList() {
        sharedPreferences = getSharedPreferences("com.example.hw5_final", MODE_PRIVATE)
        sharedPreferences.edit().remove("caloriesList").apply()
        sharedPreferences.edit().putString("caloriesList", ObjectSerializer.serialize(caloriesList)).apply()

    }//updateData

    private fun updateDigestList() {
        sharedPreferences = getSharedPreferences("com.example.hw5_final", MODE_PRIVATE)
        sharedPreferences.edit().remove("digestList").apply()
        sharedPreferences.edit().putString("digestList", ObjectSerializer.serialize(digestList)).apply()

    }//updateData

    private fun updateCalories() {
        sharedPreferences = getSharedPreferences("com.example.hw5_final", MODE_PRIVATE)
        sharedPreferences.edit().remove("caloriesDate").apply()
        sharedPreferences.edit().putString("caloriesDate", LocalDate.now().toString()).apply()

    }//updateData

    fun onSave(view : View){
        var error = false;
        if (caloriesChange.text.toString() == "" || logName.text.toString() == "" ||
            caloriesChange.text.toString().toInt() == 0) {
            error = true
            val toast = Toast.makeText(applicationContext, "Value contains errors", Toast.LENGTH_SHORT)
            toast.show()
        }
        if (!error) {
            caloriesChanged = caloriesChange.text.toString().toInt()
            description = logName.text.toString()
            val newCalories = Calories(type,description, caloriesChanged.toString())
            caloriesList.add(newCalories)
            if(digestList.size != 0){
                //modify the lastest same date digest
                if(digestList[digestList.size-1].date != LocalDate.now().toString())
                {
                    var sum = 0;
                    for ( c in caloriesList){
                        if (c.type == "food")
                            sum += c.calories.toInt()
                        else{
                            sum -= c.calories.toInt()
                        }
                    }
                    sum -= 1800 // base cost of breathing
                    val newDD = DailyDigest(sum.toString(), LocalDate.now().toString())
                    digestList.add(newDD)
                }
                else{
                    var sum = 0;
                    for ( c in caloriesList){
                        if (c.type == "food")
                            sum += c.calories.toInt()
                        else{
                            sum -= c.calories.toInt()
                        }
                    }
                    sum -= 1800 // base cost of breathing
                    val newDD = DailyDigest(sum.toString(), LocalDate.now().toString())
                    digestList.set(digestList.size-1, newDD)
                }
            }
            else{
                var sum = 0;
                for ( c in caloriesList){
                    if (c.type == "food")
                        sum += c.calories.toInt()
                    else{
                        sum -= c.calories.toInt()
                    }
                }
                sum -= 1800 // base cost of breathing
                val newDD = DailyDigest(sum.toString(), LocalDate.now().toString())
                digestList.add(newDD)
            }
            updateDigestList()
            updateCalories()
            updateCaloriesList()
            finish()
        }
    }

    fun onCancel(view : View){
        finish()
    }
}