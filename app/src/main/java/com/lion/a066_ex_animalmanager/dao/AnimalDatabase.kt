package com.lion.team1_project.dao


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lion.team1_project.vo.AnimalVO


@Database(entities = [AnimalVO::class], version = 1, exportSchema = true)
abstract class AnimalDatabase : RoomDatabase(){
    abstract fun animalDAO() : AnimalDAO

    companion object{
        var animalDatabase:AnimalDatabase? = null
        @Synchronized
        fun getInstance(context: Context) : AnimalDatabase?{
            synchronized(AnimalDatabase::class){
                animalDatabase = Room.databaseBuilder(
                    context.applicationContext, AnimalDatabase::class.java,
                    "Team1Animal.db"
                ).build()
            }
            return animalDatabase
        }

        fun destroyInstance(){
            animalDatabase = null
        }
    }
}