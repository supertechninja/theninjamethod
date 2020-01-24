package com.mcwilliams.theninjamethod.model

data class Exercise(val exerciseName:String, val exerciseType : String)

data class Data (val data : List<Exercise>)

data class Response (val updated : Int)