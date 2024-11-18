package com.lion.a066ex_animalmanager.viewmodel

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