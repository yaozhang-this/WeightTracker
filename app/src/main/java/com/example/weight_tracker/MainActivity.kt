package com.example.weight_tracker
//hw5
//Alexandra Giorno + Yao Zhang
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

var currentWeight = 0
var targetWeight = 0
var goalDate = ""
var weightList = ArrayList<Weight>()
var caloriesList = ArrayList<Calories>()
var caloriesDate = ""
var digestList = ArrayList<DailyDigest>()
lateinit var sharedPreferences : SharedPreferences

class MainActivity : AppCompatActivity() {
    lateinit var button : Button
    lateinit var currentText : EditText
    lateinit var targetText : EditText
    lateinit var dateText : DatePicker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Weight Tracker")
        button = findViewById(R.id.button)
        currentText = findViewById(R.id.currentWeight)
        targetText = findViewById(R.id.targetWeight)
        dateText = findViewById(R.id.datePicker)
        getData()

    }
    fun getData() {
        //skip this page if there are data already
        sharedPreferences = getSharedPreferences("com.example.hw5_final", MODE_PRIVATE)
        try {
            targetWeight = sharedPreferences.getString("targetWeight", "0")!!.toInt()
            goalDate = sharedPreferences.getString("goalDate", "").toString()
            weightList = ObjectSerializer.deserialize(sharedPreferences
                .getString("weightList", ObjectSerializer.serialize(ArrayList<Weight>()))) as ArrayList<Weight>
            caloriesList = ObjectSerializer.deserialize(sharedPreferences
                .getString("caloriesList", ObjectSerializer.serialize(ArrayList<Calories>()))) as ArrayList<Calories>
            System.out.println(targetWeight)
            System.out.println(goalDate)
            if (targetWeight != 0 && goalDate != "")
            {
                val intent = Intent(this, TodayDashboardActivity::class.java)
                startActivity(intent)
            }
        }
        catch (e: Exception){
            val toast = Toast.makeText(applicationContext, "No data stored", Toast.LENGTH_SHORT)
            toast.show()
        }
    }//getData

    private fun updateSingle(key : String, value : String) {
        sharedPreferences = getSharedPreferences("com.example.hw5_final", MODE_PRIVATE)
        sharedPreferences.edit().remove(key).apply()
        sharedPreferences.edit().putString(key, value).apply()

    }//updateData

    private fun updateWeightList() {
        sharedPreferences = getSharedPreferences("com.example.hw5_final", MODE_PRIVATE)
        sharedPreferences.edit().remove("weightList").apply()
        sharedPreferences.edit().putString("weightList", ObjectSerializer.serialize(weightList)).apply()

    }//updateData

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

    fun onSubmit(view : View){
        var error = false;
        if (currentText.text.toString() == "" || targetText.text.toString() == "" || targetText.text.toString() == ""
            || currentText.text.toString().toInt() == 0 || targetText.text.toString().toInt() == 0) {
            error = true
            val toast = Toast.makeText(applicationContext, "Value can not be empty", Toast.LENGTH_SHORT)
            toast.show()
        }
        val sb = StringBuilder()
        sb.append(dateText.year)
        sb.append("-")
        if (dateText.month < 10) sb.append("0")
        sb.append(dateText.month +1)
        sb.append("-")
        if (dateText.dayOfMonth < 10) sb.append("0")
        sb.append(dateText.dayOfMonth)
        val temp = sb.toString()
        if (calculateCountdown(LocalDate.now(), LocalDate.parse(temp)) < 0) {
            error = true
            val toast = Toast.makeText(applicationContext, "Date is invalid", Toast.LENGTH_SHORT)
            toast.show()
        }
        if (!error){
            currentWeight = currentText.text.toString().toInt()
            weightList.add(Weight(currentWeight.toString(), LocalDate.now().toString()))
            targetWeight = targetText.text.toString().toInt()

            goalDate = temp
            updateSingle("targetWeight", targetWeight.toString())
            updateSingle("goalDate", goalDate)
            updateWeightList()
            updateCaloriesList()
            updateDigestList()
            caloriesDate = sharedPreferences.getString("caloriesDate", "")!!
            //clear calories list every day
            if (caloriesDate != "" && caloriesDate != LocalDate.now().toString())
            {
                caloriesList.clear()
            }
            if (targetWeight == currentWeight){
                val intent = Intent(this, TargetActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, TodayDashboardActivity::class.java)
                startActivity(intent)
            }

        }

    }


}