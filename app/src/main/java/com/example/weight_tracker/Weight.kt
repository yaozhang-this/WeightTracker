package com.example.weight_tracker

import java.io.Serializable

class Weight(val weight : String, val date : String): Serializable{
    override fun toString(): String {
        var toTarget = (targetWeight - weight.toInt()).toString()
        if (toTarget == "0") toTarget = "✓"
        return "$weight lbs $date ($toTarget lbs until target)"
    }
}