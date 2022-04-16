package com.example.weight_tracker

import java.time.Duration
import java.time.LocalDate
import kotlin.math.roundToInt

fun calculateCountdown(today : LocalDate, goalDate : LocalDate) : Long{
    return Duration.between(today.atStartOfDay(), goalDate.atStartOfDay()).toDays()
}

//(weight in kg - target weight in kg) * (1100 / goal date in weeks)
fun toKilo(lbs : Int): Double {
    return lbs * 0.453
}

fun calculateCaloriesDelta(currentWeight : Int, targetWeight : Int) : Int{
    val goalDateInWeeks = calculateCountdown(LocalDate.now(), LocalDate.parse(goalDate)) / 7
    return 1800 - ((toKilo(currentWeight) - toKilo(targetWeight)) * (1100 / goalDateInWeeks)).roundToInt()
}