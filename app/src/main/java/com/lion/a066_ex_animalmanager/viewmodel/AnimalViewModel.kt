package com.lion.a061ex_roomdatabase.viewmodel

import AnimalFood
import AnimalGender
import AnimalType

data class AnimalViewModel(
    var animalIdx:Int,
    var animalType:AnimalType,
    var animalName:String,
    var animalAge:Int,
    var animalGender:AnimalGender,
    var animalFavoriteSnack:String
)