package com.example.final_project_assignment_hansenbillyramades.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartEntity::class, AddressEntity::class], version = 3, exportSchema = false)
abstract class StomazonDatabase : RoomDatabase() {
    abstract fun cartDao() : CartDao
    abstract fun addressDao() : AddressDao


    companion object {
        private var INSTANCE: StomazonDatabase? = null

        fun getDatabase(context: Context) : StomazonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StomazonDatabase::class.java,
                    "stomazon_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}