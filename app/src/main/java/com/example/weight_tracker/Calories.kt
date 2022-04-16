package com.example.weight_tracker

import java.io.Serializable

class Calories (val type : String, val name : String, val calories: String): Serializable{
    override fun toString(): String {
        if (type == "food") return "$name + $calories cals"
        else return "$name - $calories cals"
    }
}